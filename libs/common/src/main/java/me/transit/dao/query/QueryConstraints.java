package me.transit.dao.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.transit.dao.query.translator.*;
import me.transit.dao.query.tuple.*;

public class QueryConstraints {
	
	private final Class<?> criteraClass;
	private final HashMap<String, List<IQueryTuple>> map = new HashMap<>();
	private final List<String> orderBy = new ArrayList<>();

	/**
	 * 
	 * @param mainClass
	 */
	protected QueryConstraints(Class<?> mainClass) {
		criteraClass = mainClass;
	}

	protected IOrmQueryTranslator translatorFactory(IQueryTuple query) {
		IOrmQueryTranslator translator = null;

		if ( query instanceof CircleTuple) {
			translator = new CircleTupleTranslator(query);
		} else if (query instanceof NumberTuple) {
			translator = new NumberTupleTranslator(query);
		} else if (query instanceof PolygonBoxTuple) {
			translator = new PolygonTupleTranslator(query);
		} else if( query instanceof RectangleTuple) {
			translator = new RectangleTupleTranslator(query);
		} else if ( query instanceof  StringTuple) {
			translator = new StringTupleTranslator(query);
		} else if ( query instanceof  TimeTuple) {
			translator = new TimeTupleTranslator(query);
		} else {
			throw new IllegalArgumentException("Unknown IQueryTuple type: " + query.getClass().getName());
		}
		return translator;
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
				Tuple item = this.translatorFactory(tuple).getCriterion();
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
