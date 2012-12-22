/**
 * 
 */
package org.transiteRepositry.server.logic.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.transiteRepositry.server.logic.DataMining;
import org.transiteRepositry.server.request.AbstractSpatialQuery;
import org.transiteRepositry.server.request.ClusterQueryRequest;
import org.transiteRepositry.server.request.GenerateHeatMapRequest;

import me.datamining.ClusteringAlgorithm;
import me.datamining.DensityEstimateAlgorithm;
import me.datamining.SpatialSamplePoint;
import me.datamining.sample.AbstractSpatialSampleData;
import me.math.grid.SpatialGridPoint;
import me.math.grid.UniformSpatialGrid;
import me.transit.dao.AgencyDao;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;
import me.utils.ColorGradient;
import me.utils.GradientParameters;

/**
 * @author meverline
 *
 */
public class DataMiningImpl implements DataMining {
	
	public enum SampleType
	{
		TransitStopSpatialSample (me.datamining.sample.TransitStopSpatialSample.class),
		ServiceDateSample(me.datamining.sample.ServiceDateSample.class);
		
		private Class<?> sampleClass;
		
		SampleType(Class<?> aClass)
		{
			sampleClass = aClass;
		}
		
		public AbstractSpatialSampleData getSampleType()
		{
			AbstractSpatialSampleData rtn = null;
			try {
				rtn = AbstractSpatialSampleData.class.cast(sampleClass.newInstance());
			} catch (Exception e) {
				DataMiningImpl.log.error(e);
			}
			return rtn;
		}
	}
	
	private static Log log = LogFactory.getLog(RouteQueryImpl.class);
	private AgencyDao agencyDao = null;
	private TransiteStopDao transiteStopDao = null;
	private DensityEstimateAlgorithm densityEstimateAlgorithm = null;
	private ClusteringAlgorithm clusterAlgorithm = null;
	
	/**
	 * @return the agencyDao
	 */
	public AgencyDao getAgencyDao() {
		return agencyDao;
	}


	/**
	 * @param agencyDao the agencyDao to set
	 */
	public void setAgencyDao(AgencyDao agencyDao) {
		this.agencyDao = agencyDao;
	}


	/**
	 * @return the transiteStopDao
	 */
	public TransiteStopDao getTransiteStopDao() {
		return transiteStopDao;
	}
	
	/**
	 * @param transiteStopDao the transiteStopDao to set
	 */
	public void setTransiteStopDao(TransiteStopDao transiteStopDao) {
		this.transiteStopDao = transiteStopDao;
	}
	
	/**
	 * @return the densityEstimateAlgorithm
	 */
	public DensityEstimateAlgorithm getDensityEstimateAlgorithm() {
		return densityEstimateAlgorithm;
	}
	
	/**
	 * @param densityEstimateAlgorithm the densityEstimateAlgorithm to set
	 */
	public void setDensityEstimateAlgorithm(
			DensityEstimateAlgorithm densityEstimateAlgorithm) {
		this.densityEstimateAlgorithm = densityEstimateAlgorithm;
	}

	/**
	 * @return the clusterAlgorithm
	 */
	public ClusteringAlgorithm getClusterAlgorithm() {
		return clusterAlgorithm;
	}

	/**
	 * @param clusterAlgorithm the clusterAlgorithm to set
	 */
	public void setClusterAlgorithm(ClusteringAlgorithm clusterAlgorithm) {
		this.clusterAlgorithm = clusterAlgorithm;
	}


	/**
	 * 
	 * @param request
	 * @return
	 */
	private List<TransitStop> query(AbstractSpatialQuery request )
	{
		StopQueryConstraint query = new StopQueryConstraint();
		
		if ( request.getAgency() != null && (! request.getAgency().isEmpty()) ) {
			query.addAgency( getAgencyDao().findByName(request.getAgency()));
		}
		
		query.addRectangleConstraint( request.getLatLonBox().getLowerLeft(),
									  request.getLatLonBox().getUpperRight());
		
		return this.getTransiteStopDao().query(query);
	}
	

	/**
	 * 
	 * @param dataList
	 * @param grid
	 * @return
	 */
	private List<SpatialSamplePoint> addDataToGrid(List<TransitStop> dataList, 
												   UniformSpatialGrid grid,
												   SampleType type)
	{
		
		List<SpatialSamplePoint> samples = new ArrayList<SpatialSamplePoint>();
		for (TransitStop stop : dataList) {
			SpatialGridPoint point = grid.findGridPont(stop.getLocation());
			if (point != null) {
				AbstractSpatialSampleData sample;
				if (point.getData() == null) {
					sample = type.getSampleType();
					point.setData(sample);
					sample.setGridPointReferenece(point);
					samples.add(sample);
				}

				sample = AbstractSpatialSampleData.class.cast(point.getData());
				sample.addSampleData(stop);
			}
		}
		return samples;
	}
	
	/* @see server.logic.DataMining#createHeatMap(server.request.GenerateHeatMapRequest)
	 */
	@Override
	public String createHeatMap(GenerateHeatMapRequest request) {
		
	    List<TransitStop> results = this.query(request);
		
		UniformSpatialGrid grid = new UniformSpatialGrid(request.getLatLonBox().getUpperLeft(),
														 request.getLatLonBox().getLowerRight(),
														 request.getDistance().toMeters());

		SampleType type = SampleType.valueOf( request.getMetric().getMetricType());
		List<SpatialSamplePoint> samples = this.addDataToGrid(results, grid, type);
				
		getDensityEstimateAlgorithm().kernalDensityEstimate(grid, samples);
		String fileName = null;
		try {
			fileName = this.writeAsPNGImage(this.createImage(grid, request.getGradianteParameters()));
		} catch (IOException e) {
			log.error("Unable to write PNGImage: ", e);
			fileName = "";
		}
		return fileName;
	}

	/* (non-Javadoc)
	 * @see server.logic.DataMining#findClusters(server.request.ClusterQueryRequest)
	 */
	@Override
	public List<SpatialGridPoint> findClusters(ClusterQueryRequest request) {
	    List<TransitStop> results = this.query(request);
		
		UniformSpatialGrid grid = new UniformSpatialGrid(request.getLatLonBox().getUpperLeft(),
														 request.getLatLonBox().getLowerRight(),
														 request.getDistance().toMeters());

		SampleType type = SampleType.valueOf( request.getMetric().getMetricType());
		@SuppressWarnings("unused")
		List<SpatialSamplePoint> samples = this.addDataToGrid(results, grid, type);
				
		getClusterAlgorithm().setRangeHi(request.getCluster().getHiRange());
		getClusterAlgorithm().setRangeLow(request.getCluster().getLowRange());
		
		return getClusterAlgorithm().findClusters(grid);
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
    private String writeAsPNGImage(BufferedImage image) throws IOException
    {
    	 File fp = File.createTempFile("TR", "");
         ImageIO.write(image, "png", fp);
         return fp.getName();
    }
	
    /**
     * 
     * @param grid
     * @param scale
     * @return
     */
    private BufferedImage createImage(UniformSpatialGrid grid,
    								 List<GradientParameters> scale)
    {
        int widthSize = 6;
        int heightSize = 6;
                
        int width = grid.getCols()*widthSize;
        int height = grid.getRows()*heightSize;
        BufferedImage imagePanel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagePanel.createGraphics();
        
        DescriptiveStatistics stats = new DescriptiveStatistics();
		for (SpatialGridPoint pt : grid.getGridPoints()) {
			stats.addValue(pt.getData().getInterpolationValue());
		}
        	                
        List<ColorGradient> gradients = new ArrayList<ColorGradient>();
        for (GradientParameters p : scale ) {
                gradients.add(p.createColorGradient(stats));
        }
        
        // create image.
        for ( int row =0; row < grid.getRows(); row++) {
                for ( int col = 0; col < grid.getCols(); col++) {
                        Color c = Color.white;
                        int alpha = 100;
                        if ( grid.get(row, col) != null &&  
                        	 grid.get(row, col).getData().getInterpolationValue() == 0 ) {
                                alpha = 0;
                        }
                        else {
                                double value = grid.get(row,col).getData().getInterpolationValue();
                                for ( ColorGradient cg : gradients) {
                                if ( cg.contains(value)) { 
                                        c =  cg.findHeatMapColor(value); 
                                        alpha = cg.getAlhpaValue();
                                }
                        }
                        }
                        g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
                        
                        int x = widthSize*col;
                        int y = heightSize*((grid.getRows()-1)-row);
                        
                        g2.fillRect(x, y, widthSize, heightSize);
                }
        }
        
        g2.dispose();
        return imagePanel;
    }


}
