package me.transit.dao.impl;

import java.sql.SQLException;

import org.hibernate.Hibernate;

import me.database.hibernate.HibernateConnection;
import me.transit.dao.TripDao;
import me.transit.database.Trip;

public class TripDaoImpl extends TransitDaoImpl<Trip> implements TripDao {
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TripDaoImpl() throws SQLException, ClassNotFoundException {
		super(Trip.class);
	}
	
	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TripDaoImpl(HibernateConnection aConnection) throws SQLException, ClassNotFoundException {
		super(Trip.class, aConnection);
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
