package me.math.grid.tiled;

import me.database.nsstore.AbstractDocument;
import me.database.nsstore.DocumentSession;
import me.database.nsstore.IDocument;
import me.math.grid.tiled.dao.TileFragmentDao;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.NumberTuple;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SptialTileCache {

	private static final String INDEX = "index";

	private final DocumentSession documentDao;
	private final TileFragmentDao tileFragmentDao;
	private static SptialTileCache theOne = null;
	
	/**
	 * 
	 * @param documentDao
	 * @param tileFragmentDao
	 */
	private SptialTileCache(DocumentSession documentDao, TileFragmentDao tileFragmentDao) {
		this.documentDao = documentDao;
		this.tileFragmentDao = tileFragmentDao;
	}
	
	/**
	 * 
	 * @param documentDao
	 * @param tileFragmentDao
	 */
	public synchronized static void initilize(DocumentSession documentDao, TileFragmentDao tileFragmentDao) {
		
		if ( theOne == null ) {
			theOne = new SptialTileCache(documentDao, tileFragmentDao);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static SptialTileCache create() {
		if ( theOne == null ) {
			throw new IllegalStateException("SptialTileCache not initlized");
		}
		return theOne;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	protected SpatialTile load(DbTiledSpatialGrid tile, int index) throws UnknownHostException
	{
		List<IQueryTuple> list = new ArrayList<>();
		
		list.add( new NumberTuple( SptialTileCache.INDEX, index, NumberTuple.LOGIC.EQ));
		@SuppressWarnings("unused")
		List<IDocument> rtn = documentDao.find(list, tile.getHeatMapName());
		
		// TODO: convert the document from BJSON to object.
		return null;
	}
	
	/**
	 * 
	 * @param tile
	 * @throws UnknownHostException 
	 */
	protected void save(DbTiledSpatialGrid heatMap, SpatialTile tile) throws SQLException,UnknownHostException
	{
		documentDao.add(tile, heatMap.getHeatMapName());
        tileFragmentDao.save(new TileFragament( tile, heatMap));

	}
	
}
