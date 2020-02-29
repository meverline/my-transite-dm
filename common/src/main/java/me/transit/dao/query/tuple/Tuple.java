package me.transit.dao.query.tuple;

import java.util.HashMap;
import java.util.Map;

public class Tuple {
	
	private final String where;
	private final Map<String, Object> parameters = new HashMap<>();
	
	/**
	 * 
	 * @param where
	 */
	public Tuple(String where) {
		this.where = where;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void add(String name, Object value) {
		parameters.put(name, value);
	}

	/**
	 * @return the where
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}
	

}
