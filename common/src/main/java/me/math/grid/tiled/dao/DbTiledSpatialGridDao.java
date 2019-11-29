package me.math.grid.tiled.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.database.hibernate.AbstractHibernateDao;
import me.math.grid.tiled.DbTiledSpatialGrid;

@SuppressWarnings("deprecation")
@Repository(value="dbTiledSpatialGridDao")
@Qualifier("dbTiledSpatialGridDao")
@Transactional
public class DbTiledSpatialGridDao extends AbstractHibernateDao<DbTiledSpatialGrid> {

	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public DbTiledSpatialGridDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(DbTiledSpatialGrid.class, aSessionFactory);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DbTiledSpatialGrid loadByName(String id) {

		try {

			Session session = getSession();
			Query<DbTiledSpatialGrid> query = session.createQuery("from DbTiledSpatialGrid as grid where grid.heatMapName = :loc");

			query.setParameter("loc", id);

			DbTiledSpatialGrid rtn = null;
			Object obj = query.uniqueResult();
			if ( obj != null ) {
				rtn = DbTiledSpatialGrid.class.cast( obj);
			}

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	
	/**
	 * 
	 */
	public void save(DbTiledSpatialGrid tile) throws SQLException
	{		
		DbTiledSpatialGrid grid = this.loadByName(tile.getHeatMapName());
		if ( grid != null ) {
			tile.setUUID(grid.getUUID());
		}
		this.save(tile);
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	public DbTiledSpatialGrid load(String name) throws SQLException
	{		
		return this.loadByName(name);
	}
	
}
