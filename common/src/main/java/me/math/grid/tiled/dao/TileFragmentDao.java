package me.math.grid.tiled.dao;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.database.hibernate.AbstractHibernateDao;
import me.database.hibernate.HibernateConnection;
import me.math.grid.tiled.TileFragament;

@Repository(value="tileFragmentDao")
public class TileFragmentDao extends AbstractHibernateDao<TileFragament> {

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TileFragmentDao() throws SQLException, ClassNotFoundException {
		super(TileFragament.class);
	}
	
	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public TileFragmentDao(HibernateConnection hibernateConnection) throws SQLException, ClassNotFoundException {
		super(TileFragament.class, hibernateConnection);
	}

}
