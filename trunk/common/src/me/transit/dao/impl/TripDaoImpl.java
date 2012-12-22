package me.transit.dao.impl;

import java.sql.SQLException;

import me.transit.dao.TripDao;
import me.transit.database.Trip;
import org.hibernate.Hibernate;

public class TripDaoImpl extends TransitDaoImpl implements TripDao {
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public TripDaoImpl() throws SQLException, ClassNotFoundException {
		super(Trip.class);
	}
		
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized Object loadById(long id, String agencyName) {
		Trip obj = Trip.class.cast(super.loadById(id, agencyName));
		
		Hibernate.initialize(obj.getAgency());
		Hibernate.initialize(obj.getShape());
		return obj;
	}
	

}
