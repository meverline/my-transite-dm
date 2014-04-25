package me.database.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public abstract class AbstractHibernateDao<T extends Serializable> {

	private Log log = LogFactory.getLog(AbstractHibernateDao.class);
	private HibernateConnection connection = null;;
	//private SessionFactory sessionFactory;    
	private Class<?> daoClass;
	
	protected AbstractHibernateDao(Class<?> aClass) throws SQLException, ClassNotFoundException {
		this.setDaoClass(aClass);
	}
	
	/**
	 * @return the connection
	 */
	public HibernateConnection getConnection() {
		return connection;
	}
	
	/**
	 * @param connection the connection to set
	 */
	public void setConnection(HibernateConnection connection) {
		this.connection = connection;
	}

	/**
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
	    return getConnection().getSessionFactory();
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
		return this.getSessionFactory().openSession();
	}

	/**
	 * @return the daoClass
	 */
	protected Class<?> getDaoClass() {
		return daoClass;
	}

	/**
	 * @param daoClass the daoClass to set
	 */
	protected void setDaoClass(Class<?> daoClass) {
		this.daoClass = daoClass;
	}

	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public synchronized void save(T item) throws SQLException {
		this.getConnection().save(item);
	}
	
	/**
	 * 
	 * @param uuid
	 * @throws SQLException
	 */
	public synchronized void delete(long uuid) throws SQLException {
				
		Session session = null;
		Transaction tx = null;
		
		try {
			session = getSessionFactory().openSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("UUID", uuid));
			tx = session.beginTransaction();
			session.delete(crit.uniqueResult());
			tx.commit();
			session.flush();
			session.close();
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}
	}
	
	/**
	 * 
	 * @param uuids
	 * @throws SQLException
	 */
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
	protected T loadByField(String id, String property) {

		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq(property, id));
			
			@SuppressWarnings("unchecked")
			T rtn = (T) crit.uniqueResult();

			session.close();
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
	public  T loadByUUID(Long id, @SuppressWarnings("rawtypes") Class aClass) {

		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("UUID", id));
			
			@SuppressWarnings("unchecked")
			T rtn = (T) crit.uniqueResult();

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	

	
}
