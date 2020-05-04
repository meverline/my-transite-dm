package me.math.grid.tiled;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.database.mongo.AbstractDocument;
import me.database.mongo.IDocumentDao;
import me.math.grid.tiled.DbTiledSpatialGrid;
import me.math.grid.tiled.IGridDocument;
import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TileFragament;
import me.math.grid.tiled.dao.TileFragmentDao;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.NumberTuple;

public class SptialTileCache {

	private final IDocumentDao documentDao;
	private final TileFragmentDao tileFragmentDao;
	private static SptialTileCache theOne = null;
	
	/**
	 * 
	 * @param documentDao
	 * @param tileFragmentDao
	 */
	private SptialTileCache(IDocumentDao documentDao, TileFragmentDao tileFragmentDao) {
		this.documentDao = documentDao;
		this.tileFragmentDao = tileFragmentDao;
	}
	
	/**
	 * 
	 * @param documentDao
	 * @param tileFragmentDao
	 */
	public synchronized static void initilize(IDocumentDao documentDao, TileFragmentDao tileFragmentDao) {
		
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
		
		list.add( new NumberTuple( IGridDocument.INDEX, index, NumberTuple.LOGIC.EQ));
		@SuppressWarnings("unused")
		List<AbstractDocument> rtn = documentDao.find(list, tile.getHeatMapName());
		
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
