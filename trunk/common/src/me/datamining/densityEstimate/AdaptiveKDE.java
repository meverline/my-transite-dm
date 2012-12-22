package me.datamining.densityEstimate;

import java.util.List;

import me.datamining.AbstractDensityEstimateAlgorithm;
import me.datamining.SpatialSamplePoint;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.sample.DefaultSample;
import me.math.EarthConstants;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;
import me.math.grid.UniformSpatialGrid;
import me.math.kdtree.INode;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class AdaptiveKDE extends AbstractDensityEstimateAlgorithm {

	private KDTree tree_ = null;
	
	/**
	 * 
	 * @param samples
	 * @param grid
	 */
	public AdaptiveKDE()
	{
	}

	/**
	 * 
	 * @param samples
	 * @param grid
	 */
	public AdaptiveKDE(List<SpatialSamplePoint> samples, UniformSpatialGrid grid)
	{
		this.init(grid, samples);
	}

    /**
     * 
     * @param list
     * @param xstats
     * @param ystats
     * @return
     */
    private double findLocalBandWidth(List<SpatialGridPoint> list, DescriptiveStatistics xstats, DescriptiveStatistics ystats)
    {
            double totalAvgLon = 0.0;
            double totalAvgLat = 0.0;
            
            int number = 0;
            for ( SpatialGridPoint point : list ) {
                totalAvgLat += point.getVertex().getLatitudeDegress();
                totalAvgLon += point.getVertex().getLongitudeDegress();
                number++;
            }
            
            totalAvgLat = totalAvgLat / number;
            totalAvgLon = totalAvgLon / number;
            
            Vertex averagePoint = new Vertex( totalAvgLat, totalAvgLon);

            double d_latitude = 0.0;
            double d_longitude = 0.0;

            for ( SpatialGridPoint pt : list ) {
                 d_latitude += pt.getVertex().getLatitudeDegress() - totalAvgLat;
                 d_longitude += pt.getVertex().getLongitudeDegress() - totalAvgLat;
                        
                 xstats.addValue(pt.getData().getValue());
                 ystats.addValue( pt.getVertex().distanceFrom(averagePoint));
            }
            
            d_latitude = d_latitude / ( number - 1.0);
            d_longitude = d_longitude / ( number - 1.0);

            return Math.abs((d_latitude * d_longitude) / ( number - 1.0));
    }
    
    /*
     * (non-Javadoc)
     * @see me.datamining.densityEstimate.DensityEstimate#kernalDensityEstimate(me.datamining.Kernel.IDensityKernel, me.datamining.bandwidth.IBandwidth, me.datamining.bandwidth.IBandwidth)
     */
    public void kernalDensityEstimate(IDensityKernel kernel, IBandwidth xsmothParm, IBandwidth ysmothParm )
    {
    	DescriptiveStatistics xlocalStats = new DescriptiveStatistics();
        DescriptiveStatistics ylocalStats = new DescriptiveStatistics();
        
        for (int r = 0; r < this.getGrid().getRows(); r++) {
           for (int c = 0; c < this.getGrid().getCols(); c++) {
                double estitmate = 0.0;
                SpatialGridPoint gridPt = this.getGrid().get(r,c);
                
                List<SpatialGridPoint> list = 
                		tree_.search(new AdaptiveRangeSearch(gridPt.getVertex(),
                											this.getGrid().getGridSpacingMeters()*32));
                                                        
                xlocalStats.clear();
                ylocalStats.clear();
                double crossCovariance = findLocalBandWidth(list, xlocalStats, ylocalStats);
                        
                double hparm = xsmothParm.bandWidth(xlocalStats.getVariance(), 2, xlocalStats);
                double hGeoParm = ysmothParm.bandWidth(crossCovariance, 2, ylocalStats);
                
                if (xlocalStats.getVariance() > 0 &&  crossCovariance > 0) {
                        
                    for (SpatialGridPoint pt : list) {
                            for (SpatialSamplePoint cnt : this.getSampleValues()) {
                            	double t = 0;
                        	    if ( pt.getData() != null ) {
                        	    	t = pt.getData().getValue() - cnt.getValue();
                        	    } else {
                        	    	pt.setData( new DefaultSample());
                        	    }
                               
                                double X = (1.0 / hparm)* kernel.kernelValue(t / hparm);

                                double dist = gridPt.getVertex().distanceFrom(cnt.toVertex());
                                double Y = (1.0 / hGeoParm) * kernel.kernelValue(dist / hGeoParm);

                                estitmate += (Math.sqrt(Math.abs(Math.pow(X, 2))
                                                                                + Math.abs(Math.pow(Y, 2))));
                            }
                    }
                }

                gridPt.getData().setInterpolationValue( (1.0 / xlocalStats.getN())* estitmate);
            }
        }

        return;
 
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    public class AdaptiveRangeSearch extends RangeSearch 
    {
            
            public AdaptiveRangeSearch(Vertex pt, double distanceInMeters) {
                    super(pt, distanceInMeters);
            }

            @Override
            public void compare(INode point) {
                    double distance = EarthConstants.distanceMeters(getPoint(), point.getPointVertex());
                    if ( distance <= getDistanceInMeters()  ) {
                            if (point.getPoint() instanceof SpatialGridPoint) {
                            	SpatialGridPoint ip = SpatialGridPoint.class.cast(point.getPoint());
                                    if  ( ip.getData() != null ) {
                                       getList().add(point.getPoint());
                                    }
                            }
                    }
            }
                    
    }

    
}
