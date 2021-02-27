/**
 * 
 */
package me.datamining.mapreduce;

import lombok.extern.apachecommons.CommonsLog;
import me.math.grid.SpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.tiled.SpatialTile;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;

import java.io.InputStream;
import java.util.List;

/**
 * @author markeverline
 *
 */
@CommonsLog
public class PopulateGrid implements ResultsHandler {

	private SpatialTile tile_ = null;
	private KDTree tree_ = null;
	private RangeSearch search_ = null;
	private Class<?> sampleClass_ = null;
	
	/**
	 * 
	 * @param sampleClass
	 */
	public PopulateGrid(Class<?> sampleClass)
	{
		if ( sampleClass == null ) { throw new IllegalArgumentException("sampleClass can't be null"); }
		sampleClass_ = sampleClass;
	}
	

	
	/* (non-Javadoc)
	 * @see me.datamining.mapreduce.ResultsHandler#handleResult(me.datamining.mapreduce.DataResult)
	 */
	public void handleResult(DataResult result) {
		
		if ( search_ == null ) {
			search_ = new RangeSearch(result.getPoint(), tile_.getGridSpacingMeters());
		}
		
		if ( this.tile_.getMbr().contains(result.getPoint()) ) {
			search_.reset();
			search_.setPoint(result.getPoint());
			search_.setDistanceInMeters(tile_.getGridSpacingMeters());
			
		    tree_.find(search_);
		    List<SpatialGridPoint> results = search_.getResults();
		    
		    for ( SpatialGridPoint pt : results) {
		    	pt.getData().addValue(result.getMetric());
		    }
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
		
		for (SpatialGridPoint pt : gridTile.getGrid()) {
			try {
				AbstractDataSample data = (AbstractDataSample) sampleClass_.newInstance();
				pt.setData(data);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		
		QueryResults qr = new QueryResults();
		tile_ = gridTile;
		tree_ = gridTile.getTree();
		
		return qr.read(dataStream, this);
	}

}
