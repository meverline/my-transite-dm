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

package me.crime.database.tuple;

import java.util.List;

import me.crime.database.URCCatagories;
import me.transit.dao.mongo.JongoQueryBuilder;
import me.transit.dao.query.tuple.AbstractQueryTuple;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class CatagoryTuple extends AbstractQueryTuple {

	private List<String> catagorys_ = null;

	public CatagoryTuple(List<String> start)
	{
		super(URCCatagories.class, "catagorie");
		catagorys_ = start;
	}

	@Override
	public void getCriterion(Criteria crit) {
		String [] rest = new String[catagorys_.size()];

		for ( int ndx = 0; ndx < catagorys_.size(); ndx++ ) {
			rest[ndx] = catagorys_.get(ndx).toString();
		}
		
		String name =  getAlias().getSimpleName();
		
		StringBuilder builder = new StringBuilder(name);
		builder.append(".");
		builder.append(getField());
		
		crit.createAlias(name, name);
		crit.add(Restrictions.in( builder.toString(), rest));
	}
	
	@Override
	public void getDoucmentQuery(JongoQueryBuilder query) {
		throw new UnsupportedOperationException();
	}

}
