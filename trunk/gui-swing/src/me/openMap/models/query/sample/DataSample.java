package me.openMap.models.query.sample;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import me.datamining.ClusteringAlgorithm;
import me.datamining.DensityEstimateAlgorithm;
import me.datamining.SpatialSamplePoint;
import me.datamining.sample.AbstractSpatialSampleData;
import me.datamining.sample.DefaultSample;
import me.factory.DaoBeanFactory;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.SpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.transit.database.TransitStop;

import com.vividsolutions.jts.geom.Point;

public abstract class DataSample {
	
	 public enum DataMiningType { 
		 
		 HEATMAP {
			@Override
			public List<AbstractSpatialGridPoint> process(UniformSpatialGrid grid,
												  List<SpatialSamplePoint> samples) {
				
				DensityEstimateAlgorithm kde = 
					DensityEstimateAlgorithm.class.cast(DaoBeanFactory.create().getBean(DensityEstimateAlgorithm.class));
				
				kde.kernalDensityEstimate(grid, samples);
				try {
					kde.saveGird("HEATMAP.csv", "C:/tmp");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return grid.getGridPoints();
			}
		}, 
		CLUSTER {
			@Override
			public List<AbstractSpatialGridPoint> process(UniformSpatialGrid grid,
												  List<SpatialSamplePoint> samples) {
				
				ClusteringAlgorithm alog =
					ClusteringAlgorithm.class.cast(DaoBeanFactory.create().getBean(ClusteringAlgorithm.class));
				return alog.findClusters(grid);
			}
			
		};
		 

		public abstract List<AbstractSpatialGridPoint> process(UniformSpatialGrid grid, 
				 									   List<SpatialSamplePoint> samples);
     };
    
     /**
      * Provide the proper type of data handler for the sampling of points.
      * @return
      */
     public abstract AbstractSpatialSampleData getSampleType();
         
	 /**
	  * The Data Sample has user inputs.
	  * @return
	  */
     public boolean hasUserInputs()
     {
    	 return false;
     }
     
     /**
      * The User Inputs.
      * @return
      */
     public JPanel getUserInputs()
     {
    	 return null;
     }
     
     /**
      * Process the Transite stop data into a 
      * @param data
      * @param uuperLeft
      * @param lowerLeft
      * @param gridSpaceInMeters
      * @param type
      * @return
      */
	public DataResults process(List<TransitStop> dataList,
										  Point upperLeftPt,
										  Point lowerRightPt, 
										  double gridSpaceInMeters,
										  DataSample.DataMiningType type) 
	{
		Vertex upperLeft = new Vertex( upperLeftPt.getY(), upperLeftPt.getX());
		Vertex lowerRight = new Vertex( lowerRightPt.getY(), lowerRightPt.getX());
		
		return this.process(dataList, upperLeft, lowerRight, gridSpaceInMeters, type);
	}
     
     /**
      * Process the Transite stop data into a 
      * @param data
      * @param uuperLeft
      * @param lowerLeft
      * @param gridSpaceInMeters
      * @param type
      * @return
      */
	public DataResults process(List<TransitStop> dataList,
										  Vertex upperLeft,
										  Vertex lowerRight, 
										  double gridSpaceInMeters,
										  DataSample.DataMiningType type) 
	{
		UniformSpatialGrid grid = new UniformSpatialGrid(upperLeft, 
														 lowerRight,
														 gridSpaceInMeters);

		List<SpatialSamplePoint> samples = new ArrayList<SpatialSamplePoint>();
		for (TransitStop stop : dataList) {
			SpatialGridPoint point = grid.findGridPont(stop.getLocation());
			if (point != null) {
				AbstractSpatialSampleData sample;
				if (point.getData() == null) {
					sample = getSampleType();
					point.setData(sample);
					sample.setGridPointReferenece(point);
					samples.add(sample);
				}
				
				sample = AbstractSpatialSampleData.class.cast(point.getData());
				sample.addSampleData(stop);
			}
		}
		
		for ( AbstractSpatialGridPoint point : grid.getGridPoints() ) {
			if ( point.getData() == null ) {
				point.setData( new DefaultSample());
			}
		}
		
		return new DataResults(grid, type.process(grid, samples));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////

	public class DataResults {
		
		public UniformSpatialGrid grid = null;
		public List<AbstractSpatialGridPoint> results = null;
		
		public DataResults(UniformSpatialGrid aGrid, List<AbstractSpatialGridPoint> list) {
			grid = aGrid;
			results = list;
		}
	}

}
