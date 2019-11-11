package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.database.hibernate.HibernateConnection;
import me.transit.database.ServiceDate;

@Repository(value="serviceDateDao")
@Scope("singleton")
public class ServiceDateDao extends TransitDao<ServiceDate>  {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ServiceDateDao() throws SQLException, ClassNotFoundException {
		super(ServiceDate.class);
	}
	
	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public ServiceDateDao(HibernateConnection hibernateConnection) throws SQLException, ClassNotFoundException {
		super(ServiceDate.class, hibernateConnection);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized ServiceDate loadById(String id, String agencyName) {
		ServiceDate rtn = super.loadById(id, agencyName);
		
		Hibernate.initialize(rtn.getAgency());
		return rtn;
	}
		
}
