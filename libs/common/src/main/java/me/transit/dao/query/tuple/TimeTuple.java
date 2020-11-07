package me.transit.dao.query.tuple;

import java.util.Calendar;

public class TimeTuple extends AbstractQueryTuple {

	private final Calendar startTime;
	private final Calendar endTime;

	/**
	 * 
	 * @param field
	 * @param start
	 * @param end
	 */
	public TimeTuple(String field, Calendar start, Calendar end)
	{
		super(null, field);
		startTime = start;
		endTime = end;
	}
	
	/**
	 * 
	 * @param aClass
	 * @param field
	 * @param start
	 * @param end
	 */
	public TimeTuple(Class<?> aClass, String field, Calendar start, Calendar end)
	{
		super(aClass, field);
		startTime = start;
		endTime = end;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

}
