package me.transit.dao.query.tuple;

import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mongodb.BasicDBObject;

public class StringTuple extends AbstractQueryTuple {
	
	public enum MATCH {
		START {
			public String getRestriction( String value)
			{
				return "%" + value;
			}
			
			public String docuument( String value) {
				return "/." +value + "/";
			}
		},
		END {
			public String getRestriction(String value)
			{
				return value + "%";
			}
			
			public String docuument( String value) {
				return "/" +value + "./";
			}
		},
		CONTAINS {
			public String getRestriction( String value)
			{
				return "%" + value + "%";
			}
			
			public String docuument( String value) {
				return "/." +value + "./";
			}
		},
		EXACT {
			public String getRestriction(String value)
			{
				return value;
			}
			
			public String docuument( String value) {
				return  value;
			}
		};
		
		/**
		 * 
		 * @param value
		 */
		public abstract String getRestriction( String value);
		
		public abstract String docuument( String value);
	}
	
	private String value = "";
	private MATCH matchType = null;

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

	/**
	 * 
	 */
	@Override
	public void getCriterion(Criteria crit) {
		
		if ( getAlias() != null ) {
			String name =  getAlias().getSimpleName().toLowerCase();
			
			StringBuilder builder = new StringBuilder(name);
			builder.append(".");
			builder.append(getField());
			
			crit.createAlias( name, name).add(Restrictions.like(builder.toString(), matchType.getRestriction(value)));
			
		} else {
			crit.add(Restrictions.like(getField(), matchType.getRestriction(value)));
		}
	}
	
	@Override
	public void getDoucmentQuery(BasicDBObject query) {

		if ( matchType == MATCH.EXACT) {
			query.put(getField(), value );
		} else {
			query.put(getField(), Pattern.compile(matchType.docuument(value), Pattern.CASE_INSENSITIVE) );
		}
	}
}
