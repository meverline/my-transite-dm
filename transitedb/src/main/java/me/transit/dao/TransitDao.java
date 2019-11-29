package me.transit.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;

public abstract class TransitDao<T extends Serializable> extends AbstractHibernateDao<T> {

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
	@SuppressWarnings("deprecation")
	public  List<Long> findByAgency(Agency agency) throws SQLException {
		
		List<Long> rtn = new ArrayList<Long>();
		
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.createAlias("agency", "agency").add(
					Restrictions.eq("agency.name", agency.getName()));
			crit.setProjection(Projections.id());			
			
			for (Object obj : crit.list()) {
				rtn.add( Long.class.cast(obj));
			}

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
		}

		return null;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#loadById(long, java.lang.String)
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public  T loadById(String id, String agencyName) {
		
		List<Object> aList = null;
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("id", id));
			crit.createAlias("agency", "agency").add(
									Restrictions.eq("agency.name", agencyName));
			
			aList = crit.list();			
			session.close();
			if ( aList.size() < 1 ) {
				getLog().info(" Unable to find Id: " + id + " agency " + agencyName);
				return null;
			}
			return (T) aList.get(0);

		} catch (Exception ex) {
			getLog().error(getDaoClass().getName() + 
						   ": " + id + 
						   " " + agencyName +
						   " " + ex.getClass().getName() +
						   " " + ex.getLocalizedMessage());
		}

		return null;
	}

}
