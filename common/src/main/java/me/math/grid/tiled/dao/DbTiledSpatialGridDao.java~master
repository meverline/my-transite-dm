package me.math.grid.tiled.dao;

import java.sql.SQLException;

import me.database.hibernate.AbstractHibernateDao;
import me.math.grid.tiled.DbTiledSpatialGrid;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

@SuppressWarnings("deprecation")
public class DbTiledSpatialGridDao extends AbstractHibernateDao<DbTiledSpatialGrid> {

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public DbTiledSpatialGridDao() throws SQLException, ClassNotFoundException {
		super(DbTiledSpatialGrid.class);
	}
	
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
	
}
