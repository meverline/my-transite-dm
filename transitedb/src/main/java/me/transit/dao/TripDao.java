package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.transit.database.Trip;

@Repository(value="tripDao")
@Scope("singleton")
@Transactional
public class TripDao extends TransitDao<Trip>  {

	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public TripDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(Trip.class, aSessionFactory);
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
