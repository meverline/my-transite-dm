package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.transit.database.ServiceDate;

@Repository(value="serviceDateDao")
@Scope("singleton")
public class ServiceDateDao extends TransitDao<ServiceDate>  {

	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public ServiceDateDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(ServiceDate.class, aSessionFactory);
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
