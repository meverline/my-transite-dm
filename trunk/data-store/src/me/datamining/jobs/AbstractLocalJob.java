/**
 * 
 */
package me.datamining.jobs;

import java.util.Iterator;
import java.util.List;

import me.datamining.AbstractDMJob;
import me.datamining.metric.AbstractSpatialMetric;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import me.transit.database.TransitStop;

/**
 * @author markeverline
 *
 */
public abstract class AbstractLocalJob extends AbstractDMJob {

	protected UniformSpatialGrid grid_ = null;
	protected KDTree tree_ = null;

	/* (non-Javadoc)
	 * @see me.datamining.AbstractDMJob#init(me.math.Vertex, me.math.Vertex, double)
	 */
	@Override
	public void init(Vertex upperLeft, Vertex lowerRight,
					 double gridSpaceInMeters) {
		grid_ = new UniformSpatialGrid(upperLeft, lowerRight,gridSpaceInMeters);
		tree_ = grid_.getTree();

	}
	
	/* (non-Javadoc)
	 * @see me.datamining.DMJob#process(java.util.Iterator, me.datamining.metric.AbstractSpatialMetric)
	 */
	@Override
	public boolean process(Iterator<TransitStop> dataList, AbstractSpatialMetric metric)
	{
		RangeSearch search = null;
		
		while( dataList.hasNext() ) {
			TransitStop stop = dataList.next();
			
			if ( search == null ) {
				search = new RangeSearch(stop.getLocation(), 10);
			}
			search.reset();
			search.setPoint(stop.getLocation());
			
			List<AbstractSpatialGridPoint> aList = tree_.find(search);
			double value = metric.getMetric(stop);
			
			for (AbstractSpatialGridPoint pt : aList ) {
				if ( pt.getData() == null ) {
					pt.setData(this.getDataSample());
				}	
				pt.getData().addValue(value);
			}
		}
		return true;
	}



}
