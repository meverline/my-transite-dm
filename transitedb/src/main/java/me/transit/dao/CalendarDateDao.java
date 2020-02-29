package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.transit.database.CalendarDate;

@Repository(value="calendarDateDao")
@Scope("singleton")
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
	
}
