package me.transit.dao.hibernate;

import java.sql.SQLException;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class HibernateDaoImpl implements HibernateDao {

	private Log log = LogFactory.getLog(HibernateDaoImpl.class);
	private HibernateConnection connection = null;;
	//private SessionFactory sessionFactory;    
	private Class<?> daoClass;
	
	protected HibernateDaoImpl(Class<?> aClass) throws SQLException, ClassNotFoundException {
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

	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateDao#save(java.lang.Object)
	 */
	@Override
	public synchronized void save(Object item) throws SQLException {
		this.getConnection().save(item);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateDao#delete(long)
	 */
	@Override
	public synchronized void delete(long uuid) throws SQLException {
		try {
			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("UUID", uuid));
			
			session.delete(crit.uniqueResult());
			session.flush();
			session.close();
		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateDao#delete(java.util.List)
	 */
	@Override
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
	protected Object loadByField(String id, String property) {

		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq(property, id));
			
			Object rtn = crit.uniqueResult();

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateDao#loadByUUID(java.lang.Long, java.lang.Class)
	 */
	@Override
	public  Object loadByUUID(Long id, @SuppressWarnings("rawtypes") Class aClass) {

		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(this.getDaoClass());
			
			crit.add( Restrictions.eq("UUID", id));
			
			Object rtn = crit.uniqueResult();

			session.close();
			return rtn;

		} catch (HibernateException ex) {
			log.error(ex.getLocalizedMessage(), ex);
		}

		return null;

	}
	

	
}
