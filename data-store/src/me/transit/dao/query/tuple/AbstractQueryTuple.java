//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.transit.dao.query.tuple;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;


public abstract class AbstractQueryTuple implements IQueryTuple {
	
	private Class<?> alias = null;
	private String field  = null;
	
	protected AbstractQueryTuple(Class<?> aClass, String aField )
	{
		setAlias(aClass);
		setField(aField);
	}
	
	/**
	 * @return the alias
	 */
	protected Class<?> getAlias() {
		return alias;
	}
	
	/**
	 * @param alias the alias to set
	 */
	protected void setAlias(Class<?> alias) {
		this.alias = alias;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	
	/**
	 * @param field the field to set
	 */
	protected void setField(String field) {
		this.field = field;
	}

	public void getMultipeRestriction(Criteria crit) {
	}

	public Criterion getRestriction(Criteria crit) {
		return null;
	}

	public boolean hasMultipleCriterion() {
		return false;
	}

}
