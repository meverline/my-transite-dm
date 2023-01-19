package me.transit.dao;

import lombok.extern.apachecommons.CommonsLog;
import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Agency;
import me.transit.database.TransitData;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.sql.SQLException;
import java.util.List;

@CommonsLog
public abstract class TransitDao<T extends TransitData> extends AbstractHibernateDao<T> {

	/**
	 * 
	 * @param aClass
	 * @param aSessionFactory
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected TransitDao(Class<T> aClass, SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
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
			
			Root<T> root = (Root<T>) crit.from(this.getDaoClass());
			Join<T, Agency> agency_join = root.join("uuid", JoinType.INNER);

			crit.select(root.get("id"));
			crit.where(
				builder.equal(agency_join.get("name"), agency.getName())
			);
			
			alist = session.createQuery(crit).getResultList();
		
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
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
	@Transactional(readOnly = true)
	public T loadById(String id, String agencyName) {
		T rtn = null;
		
		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> crit = builder.createQuery(this.getDaoClass());
			
			Root<T> root = crit.from(this.getDaoClass());
			Join<T, Agency> agency_join = root.join("agency", JoinType.INNER);

			crit.select(root).where(
				builder.and(builder.equal(root.get("id"), id),
							builder.equal(agency_join.get("agency").get("name"), agencyName)
			));

			rtn = session.createQuery(crit).getSingleResult();

		} catch (Exception | IncompatibleClassChangeError ex) {
			log.error(getDaoClass().getName() +
						   ": id " + id +
						   ", AN " + agencyName +
						   ", (" + ex.getClass().getName() +
						   ") MSG: " + ex.getLocalizedMessage());
			throw ex;
		}

		return rtn;
	}

}
