package me.transit.dao.impl;

import java.sql.SQLException;

import me.transit.dao.ServiceDateDao;
import me.transit.database.ServiceDate;

import org.hibernate.Hibernate;

public class ServiceDateDaoImpl extends TransitDaoImpl<ServiceDate> implements ServiceDateDao {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ServiceDateDaoImpl() throws SQLException, ClassNotFoundException {
		super(ServiceDate.class);
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
