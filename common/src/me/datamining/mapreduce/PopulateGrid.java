/**
 * 
 */
package me.datamining.mapreduce;

import java.io.InputStream;
import java.util.List;

import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.tiled.SpatialTile;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author markeverline
 *
 */
public class PopulateGrid implements ResultsHandler {

	@XStreamOmitField()
	private Log logger = LogFactory.getLog(PopulateGrid.class);
	
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
			search_ = new RangeSearch(result.getPoint(), tile_.getGridSizeInMeters());
		}
		
		if ( this.tile_.getMbr().contains(result.getPoint()) ) {
			search_.reset();
			search_.setPoint(result.getPoint());
			search_.setDistanceInMeters(tile_.getGridSizeInMeters());
			
		    tree_.find(search_);
		    List<AbstractSpatialGridPoint> results = search_.getResults();
		    
		    for ( AbstractSpatialGridPoint pt : results) {
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
		
		for (AbstractSpatialGridPoint pt : gridTile.getGrid()) {
			try {
				AbstractDataSample data = (AbstractDataSample) sampleClass_.newInstance();
				pt.setData(data);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		QueryResults qr = new QueryResults();
		tile_ = gridTile;
		tree_ = gridTile.getTree();
		
		return qr.read(dataStream, this);
	}

}
