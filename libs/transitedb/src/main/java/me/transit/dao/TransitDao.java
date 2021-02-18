package me.transit.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;
import me.transit.database.TransitData;
import org.springframework.transaction.annotation.Transactional;

public abstract class TransitDao<T extends TransitData> extends AbstractHibernateDao<T> {

	/**
	 * 
	 * @param aClass
	 * @param aSessionFactory
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected TransitDao(Class<?> aClass, SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(aClass, aSessionFactory);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#deleteByAgency(me.transit.database.Agency)
	 */
	@Transactional(readOnly = true)
	public synchronized void deleteByAgency(Agency agency) throws SQLException {
		List<Long> array = this.findByAgency(agency);
		this.delete(array);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#findByAgency(me.transit.database.Agency)
	 */
	@Transactional(readOnly = true)
	public  List<Long> findByAgency(Agency agency) throws SQLException {
		List<Long> alist = null;
		
		try  {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> crit = builder.createQuery(Long.class);
			
			@SuppressWarnings("unchecked")
			Root<T> root = (Root<T>) crit.from(this.getDaoClass());
			crit.select(root.get("id"));
			crit.where(
				builder.equal(root.get("agency").get("name"), agency.getName())
			);
			
			alist = session.createQuery(crit).getResultList();
		
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}
		return alist;
	}
	
	/**
	 * 
	 * @param rtn
	 */
	protected void initObject(T rtn) {
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#loadById(long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public T loadById(String id, String agencyName) {
		T rtn = null;
		
		try {
			Session session = getSession();
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
				rtn = aList.get(0);
			}

		} catch (Exception ex) {
			getLog().error(getDaoClass().getName() + 
						   ": " + id + 
						   " " + agencyName +
						   " " + ex.getClass().getName() +
						   " " + ex.getLocalizedMessage());
		}

		return rtn;
	}

}
