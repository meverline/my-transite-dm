package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.transit.database.CalendarDate;

@Repository(value="calendarDateDao")
@Scope("singleton")
@Transactional
public class CalendarDateDao extends TransitDao<CalendarDate> {

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public CalendarDateDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(CalendarDate.class, aSessionFactory);
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
