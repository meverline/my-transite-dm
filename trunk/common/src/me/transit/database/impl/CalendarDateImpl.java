package me.transit.database.impl;

import java.util.Calendar;
import me.transit.database.CalendarDate;

public class CalendarDateImpl extends TransitDateImpl implements CalendarDate {

	private static final long serialVersionUID = 1L;
	private Calendar date = null;
	private ExceptionType  exceptionType = ExceptionType.UNKNOWN;
	
	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the exceptionType
	 */
	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	/**
	 * @param exceptionType the exceptionType to set
	 */
	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder( "CalendarData: {" + super.toString() + "}");
		
		builder.append("date: " + this.getDate().getTime().toString());
		builder.append("\n");
		builder.append("exceptionType: " + this.getExceptionType());
		builder.append("\n");
		return builder.toString();
	}
	
	/**
	 * 
	 */
	public boolean valid() 
	{
		return true;
	}

}
