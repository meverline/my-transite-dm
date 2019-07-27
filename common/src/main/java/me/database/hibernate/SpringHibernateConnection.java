/**
 * 
 */
package me.database.hibernate;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author meverline
 *
 */
public class SpringHibernateConnection implements HibernateConnection {

	private Log log = LogFactory.getLog(SpringHibernateConnection.class);
	
	@Autowired
	private SessionFactory sessionFactory; 
	
	
	public SpringHibernateConnection()
	{}
	
	public SpringHibernateConnection(SessionFactory aSessionFactory) {
		this.setSessionFactory(aSessionFactory);
	}
		
	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateConnection#getSessionFactory()
	 */
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
	public synchronized void save(Object item) throws SQLException {
		
		Session session = null;
		Transaction tx = null;
		
		try {

			session = getSessionFactory().openSession();
			tx = session.beginTransaction();

			session.saveOrUpdate(item);
			tx.commit();
			session.flush();
			session.close();

		} catch (Exception ex) {
			if ( session != null ) {
				tx.rollback();
				session.close();
		    }
			
			log.error(ex);
			throw new SQLException(ex.getLocalizedMessage());
		}
	}
	
}
