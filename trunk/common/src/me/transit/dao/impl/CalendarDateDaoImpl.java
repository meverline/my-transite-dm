package me.transit.dao.impl;

import java.sql.SQLException;

import org.hibernate.Hibernate;

import me.transit.dao.CalendarDateDao;
import me.transit.database.CalendarDate;

public class CalendarDateDaoImpl extends TransitDaoImpl implements CalendarDateDao {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public CalendarDateDaoImpl() throws SQLException, ClassNotFoundException {
		super(CalendarDate.class);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized Object loadById(String id, String agencyName) {
		CalendarDate rtn = CalendarDate.class.cast(super.loadById(id, agencyName));
		
		Hibernate.initialize(rtn.getAgency());
		return rtn;
	}
	
}
