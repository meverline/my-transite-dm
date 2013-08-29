package me.transit.dao.query.tuple;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;

public class NumberTuple extends AbstractQueryTuple {
	
	public enum LOGIC { 
		EQ {
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.eq(field, value));
			}
			
			public void docuument(BasicDBObject query, Number value, String field) {
				query.put(field, value);
			}

		},
		NEQ {
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.not(Restrictions.eq(field, value)));
			}
			
			public void docuument(BasicDBObject query, Number value, String field) {
				query.put(field, new BasicDBObject( QueryOperators.NE, value));
			}
		},
		GT{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.gt(field, value));
			}
			
			public void docuument(BasicDBObject query,  Number value, String field) {
				query.put(field, new BasicDBObject( QueryOperators.GT, value));
			}
		},
		GEQ{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.ge(field, value));
			}
			
			public void docuument(BasicDBObject query,  Number value, String field) {
				query.put(field, new BasicDBObject( QueryOperators.GTE, value));
			}
		},
		NGT{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.not(Restrictions.gt(field, value)));
			}
			
			public void docuument(BasicDBObject query, Number value, String field) {
				query.put(field, new BasicDBObject("$not", new BasicDBObject( QueryOperators.GT, value)));
			}
		},
		NGEQ{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.not(Restrictions.gt(field, value)));
			}
			
			public void docuument(BasicDBObject query, Number value, String field) {
				query.put(field, new BasicDBObject("$not", new BasicDBObject( QueryOperators.GTE, value)));
			}
		},
		LT{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.lt(field, value));
			}
			
			public void docuument(BasicDBObject query,  Number value, String field) {
				query.put(field, new BasicDBObject( QueryOperators.LT, value));
			}
		},
		LEQ{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.le(field, value));
			}
			
			public void docuument(BasicDBObject query, Number value, String field) {
				query.put(field, new BasicDBObject( QueryOperators.LTE, value));
			}
		},
		NLT{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.not(Restrictions.lt(field, value)));
			}
			
			public void docuument(BasicDBObject query, Number value, String field) {
				query.put(field, new BasicDBObject("$not", new BasicDBObject( QueryOperators.LT, value)));
			}
		},
		NLEQ{
			public void restriction(Criteria crit, Number value, String field) {
				crit.add( Restrictions.not(Restrictions.le(field, value)));
			}
			
			public void docuument(BasicDBObject query, Number value, String field) {
				query.put(field, new BasicDBObject("$not", new BasicDBObject( QueryOperators.LTE, value)));
			}
		};
		
		public abstract void restriction(Criteria crit, Number value, String field);
		public abstract void docuument(BasicDBObject query, Number value, String field);
		
	}
	
	private LOGIC logic = null;
	private Number hi = null;
	private Number lo = null;

	/**
	 * 
	 * @param aField
	 * @param value
	 * @param type
	 */
	public NumberTuple(String aField, Number hiValue, Number loValue) {
		super(null, aField);
		setHi(hiValue);
		setLo(loValue);
	}
	
	/**
	 * 
	 * @param aClass
	 * @param aField
	 * @param value
	 * @param type
	 */
	public NumberTuple(Class<?> aClass, String aField, Number hiValue, Number loValue) {
		super(aClass, aField);
		setHi(hiValue);
		setLo(loValue);
	}
	
	/**
	 * 
	 * @param aField
	 * @param value
	 * @param type
	 */
	public NumberTuple(String aField, Number hiValue, LOGIC logic) {
		super(null, aField);
		setHi(hiValue);
	}
	
	/**
	 * 
	 * @param aField
	 * @param value
	 * @param type
	 */
	public NumberTuple(Class<?> aClass, String aField, Number hiValue, LOGIC logic) {
		super(null, aField);
		setHi(hiValue);
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
	public void getCriterion(Criteria crit) {
		
		if ( getAlias() != null ) {
			String name =  getAlias().getSimpleName();
			crit.createAlias( name, name);
			
			StringBuilder builder = new StringBuilder(name);
			builder.append(".");
			builder.append(getField());
			if ( getLo() == null ) {
				getLogic().restriction(crit, hi, builder.toString()); 
			} else {
				crit.add(Restrictions.between(builder.toString(), getHi(), getLo()));
			}
				
		} else {
			if ( getLo() == null ) {
				getLogic().restriction(crit, hi, getField()); 
			} else {
				crit.add(Restrictions.between(getField(), getHi(), getLo()));
			}
		}
	}
	
	public void getDoucmentQuery(BasicDBObject query) {
		
		if ( getLo() == null ) {
			getLogic().docuument(query, hi, getField());
		} else {
			throw new IllegalArgumentException("Between operator not supported");
		}
	}

}
