package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.transit.database.Trip;

@Repository(value="tripDao")
@Scope("singleton")
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
	
	/*
	 * 	
	 */
	@Override
	protected void initObject(Trip rtn) {
		super.initObject(rtn);
		Hibernate.initialize(rtn.getShape());
	}

}
