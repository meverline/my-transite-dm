//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
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

package me.crime.database.tuple;

import java.util.Calendar;

import me.crime.database.Crime;
import me.transit.dao.query.tuple.AbstractQueryTuple;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mongodb.BasicDBObject;


public class StartDateTuple extends AbstractQueryTuple {

	private Calendar startDate_ = null;

	public StartDateTuple(Calendar start)
	{
		super(Crime.class, "startDate");
		setStartDate(start);
	}

	public void setStartDate(Calendar startDate)
	{
		startDate_ = startDate;
	}

	public void getCriterion(Criteria crit) {
		
		StringBuilder builder = new StringBuilder(getAlias().getSimpleName());
		builder.append(".");
		builder.append(getField());
		
		crit.add( Restrictions.ge( builder.toString(), startDate_));
	}
	
	@Override
	public void getDoucmentQuery(BasicDBObject query) {
		throw new UnsupportedOperationException();
	}

}
