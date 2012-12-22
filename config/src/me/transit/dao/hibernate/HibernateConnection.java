package me.transit.dao.hibernate;

import java.sql.SQLException;
import org.hibernate.SessionFactory;

public interface HibernateConnection {

	/**
	 *
	 * @return
	 */
	public SessionFactory getSessionFactory();
	
	/**
	 * 
	 * @param item
	 * @throws SQLException
	 */
	public void save(Object item) throws SQLException;

}