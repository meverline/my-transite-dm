package me.math.grid.tiled.dao;

import java.sql.SQLException;

import me.database.hibernate.AbstractHibernateDao;
import me.math.grid.tiled.DbTiledSpatialGrid;

public class DbTiledSpatialGridDao extends AbstractHibernateDao<DbTiledSpatialGrid> {

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public DbTiledSpatialGridDao() throws SQLException, ClassNotFoundException {
		super(DbTiledSpatialGrid.class);
	}
	
}
