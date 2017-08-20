package me.transit.database;

import java.util.Calendar;

public interface CalendarDate extends TransitData  {
	
	public enum ExceptionType { ADD_SERVICE, REMOVE_SERVICE, UNKNOWN };
	
	/**
	 * @return the date
	 */
	public Calendar getDate();
	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date);
	/**
	 * @return the exceptionType
	 */
	public ExceptionType getExceptionType();

	/**
	 * @param exceptionType the exceptionType to set
	 */
	public void setExceptionType(ExceptionType exceptionType);

}
