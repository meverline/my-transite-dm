package me.database.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class ManualHibernateConnection implements HibernateConnection {

	private  SessionFactory fact = null;
	private  Connection conn = null;
	private  Configuration cfg = null;
	private  ServiceRegistry serviceRegistry;

	private Log log = LogFactory.getLog(ManualHibernateConnection.class);
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ManualHibernateConnection() throws SQLException, ClassNotFoundException {
		try {

			cfg = new Configuration().configure();
			openConnection(cfg);
			cfg.setProperties(System.getProperties());
		    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();  
		    fact = cfg.buildSessionFactory(serviceRegistry);
		    
		} catch (MappingException e) {
			log.error(e.getLocalizedMessage(), e);
			throw e;
		} catch (HibernateException e) {
			log.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize()
	{
		try {
			this.closeConnection();
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	protected void openConnection(Configuration cfg) throws SQLException, ClassNotFoundException {

		try {

			Class.forName(cfg.getProperty("hibernate.connection.driver_class"));
			System.out.println("Driver Loaded.");
			
			String url = cfg.getProperty("hibernate.connection.url");
			String userName = cfg.getProperty("hibernate.connection.username");
			String password = cfg.getProperty("hibernate.connection.password");

			conn = DriverManager.getConnection(url, userName, password);

			System.out.println("Got Connection.");
			
		} catch (ClassNotFoundException e) {
			log.error(e.getLocalizedMessage(), e);
			throw e;
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage(), e);
			throw e;
		}

	}

	/**
	 * @throws SQLException
	 *
	 */
	public void closeConnection() throws SQLException {

		try {
			conn.close();
			System.out.println("Connection closed.");
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage(), e);
			throw e;
		}

	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateConnection#getSessionFactory()
	 */
	public SessionFactory getSessionFactory() {
		return fact;
	}

	/*
	 * (non-Javadoc)
	 * @see me.transit.dao.hibernate.HibernateConnection#save(java.lang.Object)
	 */
	public  void save(Object item) throws SQLException {

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

	/**
	 * @return the cfg
	 */
	public Configuration getConfiguration() {
		return cfg;
	}

}
