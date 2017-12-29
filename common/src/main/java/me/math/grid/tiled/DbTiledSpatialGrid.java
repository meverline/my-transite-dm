package me.math.grid.tiled;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Point;

import me.database.mongo.DocumentDao;
import me.factory.DaoBeanFactory;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.tiled.dao.DbTiledSpatialGridDao;
import me.math.grid.tiled.dao.TileFragmentDao;
import me.math.kdtree.KDTree;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.NumberTuple;

@SuppressWarnings("serial")
@Entity
@Table(name="hmt_SpatialGrid")
public class DbTiledSpatialGrid extends AbstractTiledSpatialGrid implements Serializable {
	
	@Id
	@Column(name="GRID_UUID", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long uuid_ = -1;
	
	@Column(name="heatMapName")
	private String heatMapName_ = null;
	
	private int cacheSize_ = 5;
	
	private Map<Integer,CachedTile> cache = new HashMap<Integer,CachedTile>();
	private List<CachedTile> accessTime_ = new ArrayList<CachedTile>();
	
	/**
	 * Constructor for the database.
	 * @param spacing
	 */
	public DbTiledSpatialGrid()
	{
	}
	
	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacing
	 * @throws SQLException 
	 * @throws UnknownHostException 
	 */
	public DbTiledSpatialGrid(Point ul, Point lr, double spacingInMeters) throws SQLException, UnknownHostException {
		init(spacingInMeters);
		setUpperLeft( new Vertex(ul.getX(), ul.getY()));
		setLowerRight( new Vertex(lr.getX(), lr.getY()));

		this.save();
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

		this.save();
		createGrid(getUpperLeft(), getLowerRight());
	}
	
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
	public TiledSpatialGridPoint get(int index, int row, int col) throws UnknownHostException {
		return this.getTileFromCache(index).getEntry(row, col);
	}
	
	@Override
	public AbstractSpatialGridPoint get(int index, int gridIndex) {
		AbstractSpatialGridPoint pt =  null;
		try {
			pt = this.getTileFromCache(index).getEntry(gridIndex);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return pt;
	}

	@Override
	protected void addTile(SpatialTile tile) throws UnknownHostException, SQLException {
		@SuppressWarnings("unused")
		KDTree tree = tile.getTree();
		this.save(tile);
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
	protected SpatialTile load(int index) throws UnknownHostException
	{
		DocumentDao docDao = DocumentDao.instance();
		
		List<IQueryTuple> list = new ArrayList<IQueryTuple>();
		
		list.add( new NumberTuple( SpatialTile.INDEX, new Integer(index), NumberTuple.LOGIC.EQ));
		docDao.find(list, this.getHeatMapName());
		return null;
	}
	
	/**
	 * 
	 * @param tile
	 * @throws UnknownHostException 
	 */
	protected void save(SpatialTile tile) throws SQLException,UnknownHostException
	{
		
		DocumentDao docDao = DocumentDao.instance();
        docDao.add(tile, this.getHeatMapName());
        
        TileFragmentDao dao = 
        		TileFragmentDao.class.cast(DaoBeanFactory.create().getDaoBean( TileFragmentDao.class));
        
        dao.save(new TileFragament( tile, this));

	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	public void save() throws SQLException
	{
		DbTiledSpatialGridDao dao = 
				DbTiledSpatialGridDao.class.cast(DaoBeanFactory.create().getDaoBean( DbTiledSpatialGridDao.class));
		
		DbTiledSpatialGrid grid = dao.loadByName(this.getHeatMapName());
		if ( grid != null ) {
			this.setUUID(grid.getUUID());
		}
		dao.save(this);
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	public static DbTiledSpatialGrid load(String name) throws SQLException
	{
		DbTiledSpatialGridDao dao = 
				DbTiledSpatialGridDao.class.cast(DaoBeanFactory.create().getDaoBean( DbTiledSpatialGridDao.class));
		
		return dao.loadByName(name);
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
