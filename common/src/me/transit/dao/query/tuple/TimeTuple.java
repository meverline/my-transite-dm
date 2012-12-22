package me.transit.dao.query.tuple;

import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class TimeTuple extends AbstractQueryTuple {

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
	public void getCriterion(Criteria crit) {
		if ( getAlias() != null ) {
			String name =  getAlias().getSimpleName();
			crit.createAlias( name, name);
			
			StringBuilder builder = new StringBuilder(name);
			builder.append(".");
			builder.append(getField());
			
			crit.add(Restrictions.between(builder.toString(), startTime, endTime));
		} else {
			crit.add(Restrictions.between(getField(), startTime, endTime));
		}
	}

}
