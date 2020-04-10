package me.transit.dao.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.Tuple;

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
	public Tuple getCirtera() {
		
		StringBuilder builder = new StringBuilder("select * from ");
		builder.append(this.criteraClass.getSimpleName());
		builder.append(" where ");
		
		Map<String, Object> paramaters = new HashMap<>();
		for ( String key : map.keySet() )
		{
			List<IQueryTuple> tupleList = map.get(key);
			int ndx = 0; 
			for (IQueryTuple tuple : tupleList ) {
				Tuple item = tuple.getCriterion();
				if ( ndx != 0 ) {
					builder.append(" and ");
				}
				builder.append(item.getWhere());
				paramaters.putAll(item.getParameters());
			}
		}
		
		Tuple rtn = new Tuple(builder.toString());
		rtn.getParameters().putAll(paramaters);
		return rtn;
	}
}
