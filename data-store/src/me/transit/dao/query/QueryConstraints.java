package me.transit.dao.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.transit.dao.query.tuple.IQueryTuple;

import org.hibernate.Criteria;
import org.hibernate.Session;

public class QueryConstraints {
	
	private Class<?> criteraClass = null;
	private HashMap<String, List<IQueryTuple>> map = new HashMap<String, List<IQueryTuple>>();
	private List<String> orderBy = new ArrayList<String>();

	/**
	 * 
	 * @param mainClass
	 */
	protected QueryConstraints(Class<?> mainClass) {
		criteraClass = mainClass;
	}
	
	/**
	 * 
	 */
	public void clear()
	{
		map.clear();
	}
	
	/**
	 * 
	 * @param tuple
	 */
	protected void add(IQueryTuple tuple) {
		String key = tuple.getField();
		if ( ! map.containsKey(key) ) {
		   List<IQueryTuple> list = new ArrayList<IQueryTuple>();
		   
		   map.put(key, list);
		}
		map.get(key).add(tuple);
	}
	
	public void addOrderBy(String field)
	{
		if ( field != null ) {
			orderBy.add(field);
		}
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public Criteria getCirtera(Session session) {
		
		Criteria criteria = session.createCriteria(this.criteraClass);
		for ( String key : map.keySet() )
		{
			List<IQueryTuple> tupleList = map.get(key);
			for (IQueryTuple tuple : tupleList ) {
				tuple.getCriterion(criteria);
			}
		}
		return criteria;
	}
}
