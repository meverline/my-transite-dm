package me.transit.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;
import me.transit.database.TransitData;

public abstract class TransitDao<T extends TransitData> extends AbstractHibernateDao<T> {

	/**
	 * 
	 * @param aClass
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected TransitDao(Class<?> aClass, SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(aClass, aSessionFactory);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#deleteByAgency(me.transit.database.Agency)
	 */
	public synchronized void deleteByAgency(Agency agency) throws SQLException {
		List<Long> array = this.findByAgency(agency);
		this.delete(array);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#findByAgency(me.transit.database.Agency)
	 */
	@SuppressWarnings("unchecked")
	public  List<Long> findByAgency(Agency agency) throws SQLException {
		
		Session session = null;
		List<Long> alist = null;
		
		try {

			session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<?> crit = builder.createQuery(this.getDaoClass());
			
			Root<T> root = (Root<T>) crit.from(this.getDaoClass());
			crit.select(root.get("id"));
			crit.where(
				builder.equal(root.get("agency").get("name"), agency.getName())
			);
			
			alist = (List<Long>) session.createQuery(crit).getResultList();				
		
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		} finally {
			if ( session != null ) { session.close(); }
		}

		return alist;
	}
	
	/**
	 * 
	 * @param rtn
	 */
	protected void initObject(T rtn) {
		Hibernate.initialize(rtn.getAgency());
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#loadById(long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public T loadById(String id, String agencyName) {
		
		Session session = null;
		T rtn = null;
		
		try {

			session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<?> crit = builder.createQuery(this.getDaoClass());
			
			Root<T> root = (Root<T>) crit.from(this.getDaoClass());
			
			crit.where(
				builder.equal(root.get("id"), id),
				builder.equal(root.get("agency").get("name"), agencyName)
			);
			
			List<T>  aList = (List<T>) session.createQuery(crit).getResultList();
					
			if ( aList.size() < 1 ) {
				getLog().info(" Unable to find Id: " + id + " agency " + agencyName);
			} else {
				rtn = (T) aList.get(0);
				this.initObject(rtn);
			}

		} catch (Exception ex) {
			getLog().error(getDaoClass().getName() + 
						   ": " + id + 
						   " " + agencyName +
						   " " + ex.getClass().getName() +
						   " " + ex.getLocalizedMessage());
		} finally {
			if ( session != null ) { session.close(); }
		}

		return rtn;
	}

}
