package me.transit.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.transit.dao.TransitDao;
import me.transit.dao.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public abstract class TransitDaoImpl<T extends Serializable> extends AbstractHibernateDao<T> implements TransitDao<T> {

	protected TransitDaoImpl(Class<?> aClass) throws SQLException, ClassNotFoundException {
		super(aClass);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#deleteByAgency(me.transit.database.Agency)
	 */
	@Override
	public synchronized void deleteByAgency(Agency agency) throws SQLException {
		List<Long> array = this.findByAgency(agency);
		this.delete(array);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.TransitDao#findByAgency(me.transit.database.Agency)
	 */
	@Override
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
	@Override
	@SuppressWarnings("unchecked")
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
