package me.math.grid.tiled.dao;

import java.sql.SQLException;

import me.database.hibernate.AbstractHibernateDao;
import me.math.grid.tiled.TileFragament;

public class TileFragmentDao extends AbstractHibernateDao<TileFragament> {

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TileFragmentDao() throws SQLException, ClassNotFoundException {
		super(TileFragament.class);
	}

}
