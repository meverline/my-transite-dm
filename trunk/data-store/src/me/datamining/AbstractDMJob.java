package me.datamining;

import java.util.Iterator;

import me.datamining.metric.AbstractSpatialMetric;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.transit.database.TransitStop;

public abstract class AbstractDMJob {
	
	 /**
	  * 
	  * @param upperLeft
	  * @param lowerRight
	  * @param gridSpaceInMeters
	  */
	 public abstract void init(Vertex upperLeft, Vertex lowerRight, double gridSpaceInMeters);
	 
	 /**
	  * 
	  * @param dataList
	  * @param metric
	  * @return
	  */
	 public abstract boolean process(Iterator<TransitStop> dataList, AbstractSpatialMetric metric);
	 
	 /**
	  * 
	  * @return
	  */
	 public abstract Iterator<AbstractSpatialGridPoint> getResults(double minValue);
	 
	 /**
	  * 
	  * @return
	  */
	 protected abstract AbstractDataSample getDataSample();

}
