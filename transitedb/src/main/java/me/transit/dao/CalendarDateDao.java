package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.database.hibernate.HibernateConnection;
import me.transit.database.CalendarDate;

@Repository(value="calendarDateDao")
public class CalendarDateDao extends TransitDao<CalendarDate> {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public CalendarDateDao() throws SQLException, ClassNotFoundException {
		super(CalendarDate.class);
	}
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public CalendarDateDao(HibernateConnection hibernateConnection) throws SQLException, ClassNotFoundException {
		super(CalendarDate.class, hibernateConnection);
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
