package me.transit.dao;

import me.database.hibernate.AbstractHibernateDao;
import me.transit.database.Trip;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Repository(value="tripDao")
@Scope("singleton")
@Transactional
public class TripDao extends AbstractHibernateDao<Trip> {

	/**
	 * 
	 * @param aSessionFactory
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
	protected void initObject(Trip rtn) {
		Hibernate.initialize(rtn.getShape());
	}

}
