package me.math.grid.tiled;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.math.Vertex;

import com.vividsolutions.jts.geom.Point;

@SuppressWarnings("serial")
public class DbTiledSpatialGrid extends AbstractTiledSpatialGrid implements Serializable {
	
	private long uuid_ = -1;
	private String heatMapName_ = null;
	private int cacheSize_ = 5;
	private Map<Integer,CachedTile> cache = new HashMap<Integer,CachedTile>();
	private List<CachedTile> accessTime_ = new ArrayList<CachedTile>();
	
	/**
	 * @return the uuid_
	 */
	public long getUUID() {
		return uuid_;
	}

	/**
	 * @param uuid_ the uuid_ to set
	 */
	public void setUUID(long uuid) {
		this.uuid_ = uuid;
	}

	/**
	 * @return the heatMapName_
	 */
	public String getHeatMapName() {
		return heatMapName_;
	}

	/**
	 * @param heatMapName_ the heatMapName_ to set
	 */
	public void setHeatMapName(String heatMapName) {
		this.heatMapName_ = heatMapName;
	}
	
	/**
	 * 
	 * @return
	 */
	public Point getUpperLeftCorner() {
		return this.getUpperLeft().toPoint();
	}
	
	/**
	 * 
	 * @param pt
	 */
	public void setUpperLeftCorner(Point pt) {
		this.setUpperLeft( new Vertex(pt.getX(), pt.getY()));
	}
	
	/**
	 * 
	 * @return
	 */
	public Point getLowerRightCorner() {
		return this.getLowerRight().toPoint();
	}
	
	/**
	 * 
	 * @param pt
	 */
	public void setLowerRightCorner(Point pt) {
		this.setLowerRight( new Vertex(pt.getX(), pt.getY()));
	}

	/**
	 * @return the cacheSize_
	 */
	public int getCacheSize() {
		return cacheSize_;
	}

	/**
	 * @param cacheSize_ the cacheSize_ to set
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize_ = cacheSize;
	}

	@Override
	public TiledSpatialGridPoint get(int index, int row, int col) {
		return this.getTileFromCache(index).getEntry(row, col);
	}
	
	@Override
	public TiledSpatialGridPoint get(int index, int gridIndex) {
		return this.getTileFromCache(index).getEntry(gridIndex);
	}

	@Override
	protected void addTile(SpatialTile tile) {
		this.save(tile);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	protected SpatialTile getTileFromCache(int index) {
		CachedTile entry = null;
		
		if ( ! cache.containsKey(index)) {
			if ( this.cache.size() >= this.cacheSize_) {
				entry = this.accessTime_.remove(0);
				cache.remove(entry.tile.getIndex());
			} else {
				entry = new CachedTile();
			}
			
			entry.tile = this.load(index);
			this.cache.put(entry.tile.getIndex(), entry);
			this.accessTime_.add(entry);
		} 
		
	    entry = cache.get(index);
		entry.access = System.currentTimeMillis();
		
		Collections.sort(this.accessTime_);
		return entry.tile;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	protected SpatialTile load(int index)
	{
		return null;
	}
	
	/**
	 * 
	 * @param tile
	 */
	protected void save(SpatialTile tile)
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	
	private class CachedTile implements Comparable<CachedTile> {
		public SpatialTile tile = null;
		public long access = -1;
		
		@Override
		public int compareTo(CachedTile value) {
			if ( value.access < this.access ) {
				return 1;
			} else if ( value.access > this.access ) {
				return -1;
			}
			return 0;
		}
		
	}

}
