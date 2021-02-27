package me.math.grid.tiled.dao;

import java.sql.SQLException;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.database.hibernate.AbstractHibernateDao;
import me.math.grid.tiled.DbTiledSpatialGrid;

@Repository(value="dbTiledSpatialGridDao")
@Qualifier("dbTiledSpatialGridDao")
@Transactional
@CommonsLog
public class DbTiledSpatialGridDao extends AbstractHibernateDao<DbTiledSpatialGrid> {

	/**
	 * 
	 * @param aSessionFactory
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
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public DbTiledSpatialGrid loadByName(String id) {

		DbTiledSpatialGrid rtn = null; 
		try (Session session = getSession()){

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<?> crit = builder.createQuery(this.getDaoClass());
			
			Root<?> root = crit.from(this.getDaoClass());
			
			crit.where(
					builder.equal(root.get("heatMapName"), id)
			);
			
			rtn =  (DbTiledSpatialGrid) session.createQuery(crit).getSingleResult();
		} catch (NoResultException ex) {
			rtn = null;
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return rtn;

	}
	
	/**
	 * 
	 */
	@Transactional
	public DbTiledSpatialGrid save(DbTiledSpatialGrid tile) throws SQLException
	{		
		DbTiledSpatialGrid grid = this.loadByName(tile.getHeatMapName());
		if ( grid != null ) {
			tile.setUUID(grid.getUUID());
		}
		return super.save(tile);
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public DbTiledSpatialGrid load(String name) throws SQLException
	{		
		return this.loadByName(name);
	}
	
}
