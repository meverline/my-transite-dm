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

import java.util.List;

import org.bson.Document;

import me.crime.database.URCCatagories;
import me.transit.dao.query.tuple.AbstractQueryTuple;
import me.transit.dao.query.tuple.Tuple;

public class CatagoryTuple extends AbstractQueryTuple {

	private List<String> catagorys_ = null;

	public CatagoryTuple(List<String> start)
	{
		super(URCCatagories.class, "catagorie");
		catagorys_ = start;
	}

	@Override
	public Tuple getCriterion() {
		String name =  getAlias().getSimpleName();
		StringBuilder builder = new StringBuilder(name);
		
		builder.append(".");
		builder.append(getField());
		builder.append(" in (");

		for ( int ndx = 0; ndx < catagorys_.size(); ndx++ ) {
			if ( ndx != 0) { builder.append(","); }
			builder.append(catagorys_.get(ndx).toString());
		}
		
		builder.append(")");
		
		return new Tuple(builder.toString());
	}
	
	@Override
	public void getDoucmentQuery(Document query) {
		throw new UnsupportedOperationException();
	}

}
