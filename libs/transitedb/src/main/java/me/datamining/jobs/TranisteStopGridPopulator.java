package me.datamining.jobs;

import java.util.Iterator;
import java.util.List;

import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.IDataProvider;
import me.math.grid.SpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import me.transit.database.TransitStop;

public class TranisteStopGridPopulator implements IPopulateGrid {

	@Override
	public boolean populate(Iterator<IDataProvider> dataList,
							AbstractSpatialMetric metric, 
							UniformSpatialGrid grid,
							AbstractDMJob job) {
		
		RangeSearch search = null;
		KDTree tree_ = grid.getTree();
		
		while( dataList.hasNext() ) {
			TransitStop stop = TransitStop.class.cast(dataList.next());
			
			if ( search == null ) {
				search = new RangeSearch(stop.getLocation(), 10);
			}
			search.reset();
			search.setPoint(stop.getLocation());
			
			List<SpatialGridPoint> aList = tree_.find(search);
			double value = metric.getMetric(stop);
			
			for (SpatialGridPoint pt : aList ) {
				if ( pt.getData() == null ) {
					pt.setData(job.getDataSample());
				}	
				pt.getData().addValue(value);
			}
		}
		return true;
	}

}
