package me.transit.dao.query.tuple;

public class NumberTuple extends AbstractQueryTuple {
	
	public enum LOGIC {
		EQ,
		NEQ,
		GT,
		GEQ,
		NGT,
		NGEQ,
		LT,
		LEQ,
		NLT,
		NLEQ
	}
	
	private final LOGIC logic;
	private final Number hi;
	private final Number lo;

	public NumberTuple(String aField, Number hiValue, Number loValue) {
		super(null, aField);
		logic = null;
		this.hi = hiValue;
		this.lo = loValue;
	}

	public NumberTuple(Class<?> aClass, String aField, Number hiValue, Number loValue) {
		super(aClass, aField);
		logic = null;
		this.hi = hiValue;
		this.lo = loValue;
	}

	public NumberTuple(String aField, Number hiValue, LOGIC logic) {
		super(null, aField);
		this.logic = logic;
		this.hi = hiValue;
		this.lo = null;
	}

	public NumberTuple(Class<?> aClass, String aField, Number hiValue, LOGIC logic) {
		super(aClass, aField);
		this.logic = logic;
		this.hi = hiValue;
		this.lo = null;
	}
	
	/**
	 * @return the logic
	 */
	public LOGIC getLogic() {
		return logic;
	}

	/**
	 * @return the hi
	 */
	public Number getHi() {
		return hi;
	}

	/**
	 * @return the lo
	 */
	public Number getLo() {
		return lo;
	}

}
