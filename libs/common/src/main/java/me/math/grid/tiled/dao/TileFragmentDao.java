package me.math.grid.tiled.dao;

import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.database.hibernate.AbstractHibernateDao;
import me.math.grid.tiled.TileFragament;

@Repository(value="tileFragmentDao")
public class TileFragmentDao extends AbstractHibernateDao<TileFragament> {

	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public TileFragmentDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(TileFragament.class, aSessionFactory);
	}

}
