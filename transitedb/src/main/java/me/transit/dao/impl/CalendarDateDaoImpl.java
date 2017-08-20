package me.transit.dao.impl;

import java.sql.SQLException;

import org.hibernate.Hibernate;

import me.database.hibernate.HibernateConnection;
import me.transit.dao.CalendarDateDao;
import me.transit.database.CalendarDate;

public class CalendarDateDaoImpl extends TransitDaoImpl<CalendarDate> implements CalendarDateDao {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public CalendarDateDaoImpl() throws SQLException, ClassNotFoundException {
		super(CalendarDate.class);
	}
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public CalendarDateDaoImpl(HibernateConnection aConnection) throws SQLException, ClassNotFoundException {
		super(CalendarDate.class, aConnection);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized CalendarDate loadById(String id, String agencyName) {
		CalendarDate rtn = super.loadById(id, agencyName);
		
		Hibernate.initialize(rtn.getAgency());
		return rtn;
	}
	
}
