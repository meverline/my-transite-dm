//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright � 2009 by Russ Brasser, Mark Everline and Eric Franklin
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

package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.CircleTuple;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.Tuple;
import org.locationtech.jts.geom.Polygon;

public class CircleTupleTranslator implements IOrmQueryTranslator {

	private final CircleTuple tuple;

	public CircleTupleTranslator(IQueryTuple query) {
		tuple = CircleTuple.class.cast(query);
	}
	/**
	 * 
	 */
	public Tuple getCriterion()
	{
		Polygon range = tuple.makeCircle(tuple.getCenter(), tuple.getDistanceInMeters());
		StringBuilder builder = new StringBuilder();
		if ( tuple.getAlias() != null ) {
			
			builder.append(" within(");
			builder.append(tuple.getAlias().getSimpleName());
			builder.append( ". ");
			builder.append(tuple.getField());
			builder.append(", :circle)= true ");
				
		} else {
			
			builder.append(" within(");
			builder.append(tuple.getField());
			builder.append(", :circle)= true ");
			
		}
		
		Tuple rtn = new Tuple(builder.toString());
		rtn.add("circle", range);
		return rtn;
		
	}

}

