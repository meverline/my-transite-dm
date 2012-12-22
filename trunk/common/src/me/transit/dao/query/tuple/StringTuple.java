package me.transit.dao.query.tuple;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class StringTuple extends AbstractQueryTuple {
	
	public enum MATCH {
		START {
			public void getRestriction(Criteria crit, String value)
			{
				this.restriction(crit, "%" + value);
			}
		},
		END {
			public void getRestriction(Criteria crit, String value)
			{
				this.restriction(crit, value + "%");
			}
		},
		CONTAINS {
			public void getRestriction(Criteria crit, String value)
			{
				this.restriction(crit, "%" + value + "%");
			}
		},
		EXACT {
			public void getRestriction(Criteria crit, String value)
			{
				this.restriction(crit, value);
			}
		};
		
		private String field = null;
		private Class<?> alias = null;
	
		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}

		/**
		 * @param field the field to set
		 */
		public void setField(String field) {
			this.field = field;
		}

		/**
		 * @return the alias
		 */
		public Class<?> getAlias() {
			return alias;
		}

		/**
		 * @param alias the alias to set
		 */
		public void setAlias(Class<?> alias) {
			this.alias = alias;
		}

		/**
		 * 
		 * @param crit
		 * @param value
		 */
		protected void restriction(Criteria crit, String value)
		{
			if ( getAlias() != null ) {
				String name =  getAlias().getSimpleName().toLowerCase();
				
				StringBuilder builder = new StringBuilder(name);
				builder.append(".");
				builder.append(getField());
				
				crit.createAlias( name, name).add(Restrictions.like(builder.toString(), value));
				
			} else {
				crit.add(Restrictions.like(getField(), value));
			}
		}

		/**
		 * 
		 * @param crit
		 * @param value
		 */
		public abstract void getRestriction(Criteria crit, String value);
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
		this.matchType.setField(getField());
		this.matchType.setAlias(getAlias());
		this.matchType.getRestriction(crit, value);
	}

}
