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

package me.transit.dao.query.tuple;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.mongodb.BasicDBObject;

public class PolygonBoxTuple extends AbstractQueryTuple {

	private GeometryFactory factory_  = new GeometryFactory();
	private List<Point> pointLine = null;

	/**
	 * 
	 * @param field
	 * @param aList
	 */
	public PolygonBoxTuple(String field, List<Point> aList )
	{
		super(null, field);
		pointLine = aList;
	}
	
	/**
	 * 
	 * @param alias
	 * @param field
	 * @param aList
	 */
	public PolygonBoxTuple(Class<?> alias, String field, List<Point> aList )
	{
		super(alias, field);
		pointLine = aList;
	}

	/**
	 * 
	 * @param aList
	 * @return
	 */
	private Polygon makePolygon(List<Point> aList ) {

		List<Coordinate> coords = new ArrayList<Coordinate>();
		
		for ( Point pt : aList ) {
			coords.add(pt.getCoordinate());
		}
		coords.add(coords.get(0));
		
		Coordinate array[] = new Coordinate[coords.size()];
		coords.toArray(array);

        return factory_.createPolygon(factory_.createLinearRing(array), null);
	}

	/**
	 * 
	 */
	public Tuple getCriterion() {

		Polygon range = makePolygon(this.pointLine);
		StringBuilder builder = new StringBuilder("within( ");
		
		if ( getAlias() != null ) {
			builder.append(getAlias().getSimpleName());
			builder.append(".");
		}
		builder.append(getField());
		builder.append(", :polygon)");
		
		Tuple rtn = new Tuple(builder.toString());
		rtn.add("polygon", range);
		
		return rtn;
	}
	
	public void getDoucmentQuery(Document query) {
				
        
		List<Double[]> list = new ArrayList<Double[]>();
		
		for ( Point pt : this.pointLine) {
		  Double [] data = new Double[2];
		  data[0] = pt.getCoordinate().x;
		  data[1] = pt.getCoordinate().y;
		  list.add(data);
		}
		
		Double [] data = new Double[2];
		data[0] = this.pointLine.get(0).getCoordinate().x;
		data[1] = this.pointLine.get(0).getCoordinate().y;
		
		query.put(getField(), new BasicDBObject("$geoWithin", list));

	}

}
