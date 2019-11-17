package me.transit.parser.data;

import java.sql.SQLException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.CalendarDateDao;
import me.transit.database.CalendarDate;

@Component(value = "calendarDateFileHandler")
public class CalendarDateFileHandler extends AbstractDefaultFileHandler {

	private final CalendarDateDao calendarDateDao;

	@Autowired
	public CalendarDateFileHandler(Blackboard blackboard, CalendarDateDao calendarDateDao,
			IGraphDatabaseDAO graphDatabase) {
		super(blackboard, graphDatabase);
		this.calendarDateDao = Objects.requireNonNull(calendarDateDao,"calendarDateDao can not be null");
	}
	
	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "calendar_dates.txt";
	}

	/**
	 * 
	 */
	@Override
	public void save(Object obj) throws SQLException {
		calendarDateDao.save(CalendarDate.class.cast(obj));
	}
}
