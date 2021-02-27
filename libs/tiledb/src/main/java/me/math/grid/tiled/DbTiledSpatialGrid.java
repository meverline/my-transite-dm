package me.math.grid.tiled;

import lombok.extern.apachecommons.CommonsLog;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;
import me.math.kdtree.KDTree;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("serial")
@Entity
@Table(name="hmt_SpatialGrid")
@CommonsLog
public class DbTiledSpatialGrid extends TiledSpatialGrid implements Serializable {

	@Id
	@Column(name="GRID_UUID", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long uuid_ = -1;
	
	@Column(name="heatMapName")
	private String heatMapName_ = null;
	
	private transient int cacheSize_ = 5;
	
	private transient Map<Integer,CachedTile> cache = new HashMap<>();
	private transient List<CachedTile> accessTime_ = new ArrayList<>();
	
	/**
	 * Constructor for the database.
	 */
	public DbTiledSpatialGrid()
	{
	}
	
	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacingInMeters
	 * @throws SQLException 
	 * @throws UnknownHostException 
	 */
	public DbTiledSpatialGrid(Point ul, Point lr, double spacingInMeters) throws SQLException, UnknownHostException {
		init(spacingInMeters);
		setUpperLeft( new Vertex(ul.getX(), ul.getY()));
		setLowerRight( new Vertex(lr.getX(), lr.getY()));
		createGrid(getUpperLeft(), getLowerRight());
	}
	
	/**
	 * 
	 * @param ul
	 * @param lr
	 * @param spacingInMeters
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	public DbTiledSpatialGrid(Vertex ul, Vertex lr, double spacingInMeters) throws SQLException, UnknownHostException {
		init(spacingInMeters);
		setUpperLeft(ul);
		setLowerRight(lr);
		createGrid(getUpperLeft(), getLowerRight());
	}
	
	/**
	 * @return the uuid_
	 */

	public long getUUID() {
		return uuid_;
	}

	/**
	 * @param uuid the uuid_ to set
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
	 * @param heatMapName the heatMapName_ to set
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
	 * @param cacheSize the cacheSize_ to set
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize_ = cacheSize;
	}

	@Override
	public SpatialGridPoint get(int index, int row, int col)  {
		try {
			return this.getTileFromCache(index).getEntry(row, col);
		} catch (UnknownHostException e) {
			log.error(e.getLocalizedMessage(), e);
			return null;
		}
	}
	
	@Override
	public SpatialGridPoint get(int index, int gridIndex) {
		SpatialGridPoint pt =  null;
		try {
			pt = this.getTileFromCache(index).getEntry(gridIndex);
		} catch (UnknownHostException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return pt;
	}

	@Override
	protected void addTile(SpatialTile tile)  {
		@SuppressWarnings("unused")
		KDTree tree = tile.getTree();
		try {
			SptialTileCache.create().save(this, tile);
		} catch (SQLException throwables) {
			log.error(throwables.getLocalizedMessage(), throwables);
		} catch (UnknownHostException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 * @throws UnknownHostException 
	 */
	protected SpatialTile getTileFromCache(int index) throws UnknownHostException {
		CachedTile entry = null;
		
		if ( ! cache.containsKey(index)) {
			if ( this.cache.size() >= this.cacheSize_) {
				entry = this.accessTime_.remove(0);
				cache.remove(entry.tile.getIndex());
			} else {
				entry = new CachedTile();
			}
			
			entry.tile = SptialTileCache.create().load(this, index);
			this.cache.put(entry.tile.getIndex(), entry);
			this.accessTime_.add(entry);
		} 
		
	    entry = cache.get(index);
		entry.access = System.currentTimeMillis();
		
		Collections.sort(this.accessTime_);
		return entry.tile;
	}
			
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	
	private class CachedTile implements Comparable<CachedTile> {
		public SpatialTile tile = null;
		public long access = -1;
		
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
