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

package me.transit.dao.query.translator.mongo;

import com.mongodb.BasicDBObject;
import me.transit.dao.query.tuple.CircleTuple;
import me.transit.dao.query.tuple.IQueryTuple;
import org.bson.Document;
import org.locationtech.jts.geom.Coordinate;

public class MongoCircleTuple implements IMongoQueryTranslator{

	private final CircleTuple tuple;

	public MongoCircleTuple(IQueryTuple query) {
		tuple = CircleTuple.class.cast(query);
	}

	public void getDoucmentQuery(Document query) {
				
        Coordinate coord = tuple.getCenter().getCoordinate();
        query.append(tuple.getField(), new BasicDBObject("$geoWithin",
                                                   new Object[]{ new Double[]{ coord.x , coord.y },
														   tuple.getDistanceInMeters()  }));
	}


}

