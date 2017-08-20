package me.transit.database.impl;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import me.transit.database.CalendarDate;

@Entity
@Table(name="tran_calendar_date")
public class CalendarDateImpl extends TransitDateImpl implements CalendarDate {

	private static final long serialVersionUID = 1L;
	
	private Calendar date = Calendar.getInstance();
	private ExceptionType  exceptionType = ExceptionType.UNKNOWN;
	
	/**
	 * @return the date
	 */
	@Column(name="DATE")
	@Type(type = "java.util.Calendar")
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
	@Column(name="EXCCEPTOIN_TYPE")
	@Enumerated(EnumType.STRING) 
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
