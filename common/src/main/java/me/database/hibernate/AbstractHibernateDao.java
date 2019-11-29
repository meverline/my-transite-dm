package me.database.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractHibernateDao<T extends Serializable> {

	private Log log = LogFactory.getLog(AbstractHibernateDao.class);
	
	private final SessionFactory sessionFactory; 
	private final Class<?> daoClass;
	
	protected AbstractHibernateDao(Class<?> aClass, SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		Objects.requireNonNull(aClass, "aClass cannot be null");
		Objects.requireNonNull(aSessionFactory, "aSessionFactory cannot be null");
		
		daoClass = aClass;
		sessionFactory = aSessionFactory;
	}
	
	/**
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
	    return this.sessionFactory;
	}
	
	/**
	 * @return the log
	 */
	protected Log getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	protected void setLog(Log log) {
		this.log = log;
	}

	/**
	 * @return the connection
	 */
	protected Session getSession() {
		try {
			return this.getSessionFactory().getCurrentSession();
		} catch ( Exception e) {
			return this.getSessionFactory().openSession();
		}
	}

	/**
	 * @return the daoClass
	 */
	protected final Class<?> getDaoClass() {
		return daoClass;
	}

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	@Transactional
	public synchronized void save(T item) throws SQLException {
		
		Session session = null;
		
		try {

			session = getSessionFactory().openSession();
			session.saveOrUpdate(item);
			session.flush();
			
		} catch (Exception ex) {
			if ( session != null ) {
				session.close();
		    }
			
			log.error(ex);
			throw new SQLException(ex.getLocalizedMessage());
		}
	}
	
	/**
	 * 
	 * @param uuid
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	@Transactional
	public synchronized void delete(long uuid) throws SQLException {
				
		Session session = null;
		
		try {
			session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("UUID", uuid));
			session.delete(crit.uniqueResult());
			session.flush();
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}
	}
	
	/**
	 * 
	 * @param uuids
	 * @throws SQLException
	 */
	@Transactional
	public synchronized void delete(List<Long> uuids) throws SQLException {
		for (Long uuid : uuids) {
			this.delete(uuid);
		}
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected T loadByField(String id, String property) {

		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq(property, id));
			
			@SuppressWarnings("unchecked")
			T rtn = (T) crit.uniqueResult();
			return rtn;

		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	
	/**
	 * 
	 * @param id
	 * @param aClass
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public  T loadByUUID(Long id, @SuppressWarnings("rawtypes") Class aClass) {

		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("UUID", id));
			
			@SuppressWarnings("unchecked")
			T rtn = (T) crit.uniqueResult();
			return rtn;

		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	

	
}
