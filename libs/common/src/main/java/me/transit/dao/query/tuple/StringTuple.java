package me.transit.dao.query.tuple;

public class StringTuple extends AbstractQueryTuple {
	
	public enum MATCH {
		START ,
		END ,
		CONTAINS ,
		EXACT
	}
	
	private final String value;
	private final MATCH matchType;

	/**
	 * 
	 * @param aField
	 * @param value
	 * @param type
	 */
	public StringTuple(String aField, String value, MATCH type) {
		super(null, aField);
		this.value = value;
		matchType = type;
	}
	
	/**
	 * 
	 * @param aClass
	 * @param aField
	 * @param value
	 * @param type
	 */
	public StringTuple(Class<?> aClass, String aField, String value, MATCH type) {
		super(aClass, aField);
		this.value = value;
		matchType = type;
	}

	public String getValue() {
		return value;
	}

	public MATCH getMatchType() {
		return matchType;
	}

}
