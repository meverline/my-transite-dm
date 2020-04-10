package me.transit.dao.query.tuple;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import org.bson.Document;

public class TimeTuple extends AbstractQueryTuple {
	
	public static final String SDF_DATE_FORMAT = "MMM dd yyyy";

	private Calendar startTime = null;
	private Calendar endTime = null;

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
	
	/**
	 * 
	 */
	public Tuple getCriterion() {
		StringBuilder builder = new StringBuilder();
		if ( getAlias() != null ) {
			builder.append(getAlias().getSimpleName());
			builder.append(".");
		}
		builder.append(getField());
		builder.append(" between ");
		builder.append(" :time_start ");
		builder.append(" and ");
		builder.append(" :time_end");
		
		Tuple rtn = new Tuple(builder.toString());
		rtn.add("time_start", startTime);
		rtn.add("time_end", endTime);
		return rtn;
	}
	
	/*
	 * 
	 */
	@Override
	public void getDoucmentQuery(Document query) {
        SimpleDateFormat sdf = new SimpleDateFormat( TimeTuple.SDF_DATE_FORMAT);
        
        String start = sdf.format(startTime.getTime());
        String end = sdf.format(endTime.getTime());
        query.put(getField(), new BasicDBObject(QueryOperators.GTE, start).append(QueryOperators.LTE, end) );
	}

}
