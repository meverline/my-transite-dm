/**
 * 
 */
package me.database.hibernate;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public synchronized void save(Object item) throws SQLException {
		
		Session session = null;
		
		try {

			session = getSessionFactory().openSession();
			session.saveOrUpdate(item);
			session.flush();
			session.close();

		} catch (Exception ex) {
			if ( session != null ) {
				session.close();
		    }
			
			log.error(ex);
			throw new SQLException(ex.getLocalizedMessage());
		}
	}
	
}
