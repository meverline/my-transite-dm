package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.database.hibernate.HibernateConnection;
import me.transit.database.Trip;

@Repository(value="tripDao")
@Scope("singleton")
public class TripDao extends TransitDao<Trip>  {
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TripDao() throws SQLException, ClassNotFoundException {
		super(Trip.class);
	}
	
	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public TripDao(HibernateConnection hibernateConnection) throws SQLException, ClassNotFoundException {
		super(Trip.class, hibernateConnection);
	}
		
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized Trip loadById(String id, String agencyName) {
		Trip obj = super.loadById(id, agencyName);
		
		Hibernate.initialize(obj.getAgency());
		Hibernate.initialize(obj.getShape());
		return obj;
	}
	

}
