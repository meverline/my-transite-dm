package me.datamining.jobs;

import java.util.Iterator;

import me.datamining.metric.AbstractSpatialMetric;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.grid.data.AbstractDataSample;
import me.math.kdtree.KDTree;
import me.datamining.metric.IDataProvider;

public abstract class AbstractDMJob {
	
	protected UniformSpatialGrid grid_ = null;
	protected KDTree tree_ = null;
	protected IPopulateGrid gridPopulator = null;
	
	protected AbstractDMJob(IPopulateGrid pg) {
		gridPopulator = pg;
	}
	
	 /**
	  * 
	  * @param upperLeft
	  * @param lowerRight
	  * @param gridSpaceInMeters
	  */
	 public void init(Vertex upperLeft, Vertex lowerRight, double gridSpaceInMeters) {
			grid_ = new UniformSpatialGrid(upperLeft, lowerRight,gridSpaceInMeters);
			tree_ = grid_.getTree();

	 }
	 
	 /**
	  * 
	  * @param dataList
	  * @param metric
	  * @return
	  */
	 public abstract boolean process(Iterator<IDataProvider> dataList, AbstractSpatialMetric metric);
	 
	 /**
	  * 
	  * @return
	  */
	 public abstract Iterator<AbstractSpatialGridPoint> getResults(double minValue);
	 
	 /**
	  * 
	  * @return
	  */
	 public abstract AbstractDataSample getDataSample();

}
