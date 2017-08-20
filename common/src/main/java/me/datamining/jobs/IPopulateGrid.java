package me.datamining.jobs;

import java.util.Iterator;

import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.IDataProvider;
import me.math.grid.array.UniformSpatialGrid;

public interface IPopulateGrid {

	public boolean populate(Iterator<IDataProvider> dataList, 
				            AbstractSpatialMetric metric,
				            UniformSpatialGrid grid,
				            AbstractDMJob job);
}
