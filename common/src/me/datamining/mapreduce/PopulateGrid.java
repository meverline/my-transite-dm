/**
 * 
 */
package me.datamining.mapreduce;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.tiled.SpatialTile;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;

/**
 * @author markeverline
 *
 */
public class PopulateGrid implements ResultsHandler {

	private SpatialTile tile_ = null;
	private KDTree tree_ = null;
	private RangeSearch search_ = null;
	private Class<?> sampleClass_ = null;
	private DescriptiveStatistics xstats_ = new DescriptiveStatistics();
	
	/**
	 * 
	 * @param sampleClass
	 */
	public PopulateGrid(Class<?> sampleClass)
	{
		if ( sampleClass == null ) { throw new IllegalArgumentException("sampleClass can't be null"); }
		sampleClass_ = sampleClass;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getVariance()
	{
		return xstats_.getVariance();
	}
	
	/* (non-Javadoc)
	 * @see me.datamining.mapreduce.ResultsHandler#handleResult(me.datamining.mapreduce.DataResult)
	 */
	@Override
	public void handleResult(DataResult result) {
		
		xstats_.addValue(result.getMetric());
		if ( search_ == null ) {
			search_ = new RangeSearch(result.getPoint(), tile_.getGridSizeInMeters());
		}
		search_.reset();
		search_.setPoint(result.getPoint());
		search_.setDistanceInMeters(tile_.getGridSizeInMeters());
		
	    tree_.find(search_);
	    List<AbstractSpatialGridPoint> results = search_.getResults();
	    
	    for ( AbstractSpatialGridPoint pt : results) {
	    	if ( pt.getData() == null ) {
	    		try {
					AbstractDataSample data = (AbstractDataSample) sampleClass_.newInstance();
					pt.setData(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
	 	    }
	    	pt.getData().addValue(result.getMetric());
	    }
	}
	
	/**
	 * 
	 * @param gridTile
	 * @param dataStream
	 * @return
	 */
	public boolean populate(SpatialTile gridTile, InputStream dataStream)
	{
		if ( gridTile == null ) { throw new IllegalArgumentException("gridTile can't be null"); }
		if ( dataStream == null ) { throw new IllegalArgumentException("dataStream can't be null"); }
		
		QueryResults qr = new QueryResults();
		tile_ = gridTile;
		tree_ = gridTile.getTree();
		
		return qr.read(dataStream, this);
	}

}
