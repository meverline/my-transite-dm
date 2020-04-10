package me.transit.dao.query.tuple;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import org.bson.Document;

public class NumberTuple extends AbstractQueryTuple {
	
	public enum LOGIC { 
		EQ {
            public String restriction(Number value, String field) {
                  return field + " = " + value.toString();
            }
            
            public void docuument(Document query, Number value, String field) {
                    query.put(field, value);
            }

    },
    NEQ {
            public String restriction( Number value, String field) {
            	return field + " != " + value.toString();
            }
            
            public void docuument(Document query, Number value, String field) {
                    query.put(field, new Document( QueryOperators.NE, value));
            }
    },
    GT{
            public String restriction(Number value, String field) {
            	return field + " > " + value.toString();
            }
            
            public void docuument(Document query,  Number value, String field) {
                    query.put(field, new Document( QueryOperators.GT, value));
            }
    },
    GEQ{
            public String restriction(Number value, String field) {
            	return field + " >= " + value.toString();
            }
            
            public void docuument(Document query,  Number value, String field) {
                    query.put(field, new Document( QueryOperators.GTE, value));
            }
    },
    NGT{
            public String restriction(Number value, String field) {
            	return "not " + field + " > " + value.toString();
            }
            
            public void docuument(Document query, Number value, String field) {
                    query.put(field, new Document("$not", new Document( QueryOperators.GT, value)));
            }
    },
    NGEQ{
            public String restriction(Number value, String field) {
            	return "not " + field + " >= " + value.toString();
            }
            
            public void docuument(Document query, Number value, String field) {
                    query.put(field, new Document("$not", new Document( QueryOperators.GTE, value)));
            }
    },
    LT{
            public String restriction(Number value, String field) {
            	return field + " < " + value.toString();
            }
            
            public void docuument(Document query,  Number value, String field) {
                    query.put(field, new Document( QueryOperators.LT, value));
            }
    },
    LEQ{
            public String restriction(Number value, String field) {
            	return field + " <=" + value.toString();
            }
            
            public void docuument(Document query, Number value, String field) {
                    query.put(field, new Document( QueryOperators.LTE, value));
            }
    },
    NLT{
            public String restriction(Number value, String field) {
            	return "not " + field + " < " + value.toString();
            }
            
            public void docuument(Document query, Number value, String field) {
                    query.put(field, new Document("$not", new Document( QueryOperators.LT, value)));
            }
    },
    NLEQ{
            public String restriction(Number value, String field) {
            	return "not " + field + " <=" + value.toString();
            }
            
            public void docuument(Document query, Number value, String field) {
                    query.put(field, new Document("$not", new Document( QueryOperators.LTE, value)));
            }
		};
		
		public abstract String restriction(Number value, String field);
		public abstract void docuument(Document query, Number value, String field);
	}
	
	private LOGIC logic = null;
	private Number hi = null;
	private Number lo = null;

	public NumberTuple(String aField, Number hiValue, Number loValue) {
		super(null, aField);
		setHi(hiValue);
		setLo(loValue);
	}

	public NumberTuple(Class<?> aClass, String aField, Number hiValue, Number loValue) {
		super(aClass, aField);
		setHi(hiValue);
		setLo(loValue);
	}

	public NumberTuple(String aField, Number hiValue, LOGIC logic) {
		super(null, aField);
		setHi(hiValue);
		setLogic(logic);
	}

	public NumberTuple(Class<?> aClass, String aField, Number hiValue, LOGIC logic) {
		super(aClass, aField);
		setHi(hiValue);
		setLogic(logic);
	}
	
	/**
	 * @return the logic
	 */
	protected LOGIC getLogic() {
		return logic;
	}

	/**
	 * @param logic the logic to set
	 */
	protected void setLogic(LOGIC logic) {
		this.logic = logic;
	}

	/**
	 * @return the hi
	 */
	protected Number getHi() {
		return hi;
	}

	/**
	 * @param hi the hi to set
	 */
	protected void setHi(Number hi) {
		this.hi = hi;
	}

	/**
	 * @return the lo
	 */
	protected Number getLo() {
		return lo;
	}

	/**
	 * @param lo the lo to set
	 */
	protected void setLo(Number lo) {
		this.lo = lo;
	}

	@Override
	public Tuple getCriterion() {
		
		StringBuilder builder = new StringBuilder();
		StringBuilder field = new StringBuilder();
				
		if ( getAlias() != null ) {
						
			field.append(getAlias().getSimpleName());
			field.append(".");
			field.append(getField());
		} else {
			field.append(this.getField());
		}
		
		if ( getLo() == null ) {
			builder.append(getLogic().restriction( hi, field.toString())); 
		} else {
			builder.append(field.toString());
			builder.append(" between ");
			builder.append( getHi() ); 
			builder.append( " and ");
			builder.append( getLo());
		}
		
		return new Tuple(builder.toString());
		
	}
	
	public void getDoucmentQuery(Document query) {
		
		if ( getLo() == null ) {
			getLogic().docuument(query, hi, getField());
		} else {
			throw new IllegalArgumentException("Between operator not supported");
		}
	}

}
