package me.openMap.models.query.sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import me.datamining.jobs.AbstractDMJob;
import me.datamining.jobs.ClusteringLocalJob;
import me.datamining.jobs.DensityEstimateLocalJob;
import me.datamining.jobs.IPopulateGrid;
import me.datamining.jobs.TranisteStopGridPopulator;
import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.IDataProvider;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.transit.database.TransitStop;

import com.vividsolutions.jts.geom.Point;

public abstract class DataSample {
	
	public enum DataMiningType {

		 HEATMAP {
			@Override
			public AbstractDMJob getJob(IPopulateGrid pg) {
				return new DensityEstimateLocalJob(pg);
			}
			
			
		}, 
		HADOOP_HEATMAP {
			@Override
			public AbstractDMJob getJob(IPopulateGrid pg) {
				return new DensityEstimateLocalJob(pg);
			}
		}, 
		CLUSTER {
			@Override
			public AbstractDMJob getJob(IPopulateGrid pg) {
				return new ClusteringLocalJob(pg);
			}
			
		};
		 

		public abstract AbstractDMJob getJob(IPopulateGrid pg);
			
	}
	
     /**
      * Provide the proper type of data handler for the sampling of points.
      * @return
      */
     public abstract AbstractSpatialMetric getSampleType();
         
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
										  DataMiningType type) 
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
										  DataMiningType type) 
	{
		TranisteStopGridPopulator pg = new TranisteStopGridPopulator();
		UniformSpatialGrid grid = new UniformSpatialGrid(upperLeft, lowerRight, gridSpaceInMeters);
		List<AbstractSpatialGridPoint> rtn = new ArrayList<AbstractSpatialGridPoint>();
		AbstractDMJob job = type.getJob(pg);
				
		pg.populate(new Provider(dataList), getSampleType(), grid, job);
		if ( job.process(new Provider(dataList), getSampleType()) ) {
			
			
			Iterator<AbstractSpatialGridPoint> results = job.getResults(0);
			while ( results.hasNext() ) {
				rtn.add(results.next());
			}
			
		}
		return new DataResults(rtn);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////

	
	public class Provider implements Iterator<IDataProvider> {
		
		
		private Iterator<TransitStop> it = null;
		
		public Provider(List<TransitStop> list) {
			it = list.iterator();
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public IDataProvider next() {
			return it.next();
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////

	public class DataResults {
		
		public List<AbstractSpatialGridPoint> results = null;
		
		public DataResults(List<AbstractSpatialGridPoint> list) {
			results = list;
		}
	}

}
