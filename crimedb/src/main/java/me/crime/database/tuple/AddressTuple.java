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

import com.mongodb.BasicDBObject;

import me.crime.database.Address;
import me.transit.dao.query.tuple.AbstractQueryTuple;
import me.transit.dao.query.tuple.Tuple;


public class AddressTuple extends AbstractQueryTuple {

	private String address_ = "";

	public AddressTuple(String start)
	{
		super(Address.class, "location");
		address_ = start;
	}

	@Override
	public Tuple getCriterion() {
		String name =  getAlias().getSimpleName();
		
		StringBuilder builder = new StringBuilder(name);
		builder.append(".");
		builder.append(getField());
		builder.append(" like ");
		builder.append("%" + address_ + "%");
		
		return new Tuple(builder.toString());
	}

	@Override
	public void getDoucmentQuery(BasicDBObject query) {
		throw new UnsupportedOperationException();
	}


}
