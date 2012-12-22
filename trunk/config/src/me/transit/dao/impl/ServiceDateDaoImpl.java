package me.transit.dao.impl;

import java.sql.SQLException;

import me.transit.dao.ServiceDateDao;
import me.transit.database.ServiceDate;

import org.hibernate.Hibernate;

public class ServiceDateDaoImpl extends TransitDaoImpl implements ServiceDateDao {
	
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
	public synchronized Object loadById(long id, String agencyName) {
		ServiceDate rtn = ServiceDate.class.cast(super.loadById(id, agencyName));
		
		Hibernate.initialize(rtn.getAgency());
		return rtn;
	}
		
}
