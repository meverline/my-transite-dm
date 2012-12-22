/**
 * 
 */
package me.transit.dao.hibernate;

import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author meverline
 *
 */
public class SpringHibernateConnection implements HibernateConnection {

	private SessionFactory sessionFactory;    
	
	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateConnection#getSessionFactory()
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateDao#save(java.lang.Object)
	 */
	@Override
	public synchronized void save(Object item) throws SQLException {
		Session session = getSessionFactory().getCurrentSession();
		session.save(item);
		session.flush();
		session.close();
	}
	
}
