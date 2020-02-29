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

package me.transit.dao.query.tuple;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import com.mongodb.BasicDBObject;

import me.output.KmlFormatter;

public class CircleTuple extends AbstractQueryTuple {

	private static Log log = LogFactory.getLog(CircleTuple.class);
	private GeometryFactory factory_  = new GeometryFactory();
	private double distanceInMeters_ = 0.0;
	private Point center_ = null;

	/**
	 * 
	 * @param field
	 * @param center
	 * @param distance
	 */
	public CircleTuple( String field, Point center, double distanceInMeters )
	{
		super(null, field);
		center_ = center;
		distanceInMeters_ = distanceInMeters;
	}
	
	/**
	 * 
	 * @param alias
	 * @param field
	 * @param center
	 * @param distance
	 */
	public CircleTuple( Class<?> alias, String field, Point center, double distanceInMeters )
	{
		super(alias, field);
		center_ = center;
		distanceInMeters_ = distanceInMeters;
	}

	/**
	 * 
	 */
	public boolean hasMultipleCriterion()
	{
		return true;
	}
	
	/**
	 * @return The Radius of the earth at the equator. 
	 */
	private static double equatorialRadiusInMeters()
    {
        return 6378137.0;
    }

	/**
	 * 
	 * @param pt
	 * @param distance
	 * @return
	 */
	private Polygon  makeCircle( Point location, double radiusMeters) {

		double distance = radiusMeters / CircleTuple.equatorialRadiusInMeters() * 2;
	    GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
	    shapeFactory.setNumPoints(32);
	    shapeFactory.setCentre(new Coordinate(location.getX(), location.getY()));
	    shapeFactory.setSize(radiusMeters / CircleTuple.equatorialRadiusInMeters() * 2);
	    Polygon circle = shapeFactory.createCircle();
	    	
		List<Coordinate> coords = new ArrayList<Coordinate>();
		for ( Coordinate coord : circle.getCoordinates()) {	
			coords.add( coord );
		}
		
		coords.add(coords.get(0));
		Coordinate array[] = new Coordinate[coords.size()];
		coords.toArray(array);
		
		StringBuilder kml = new StringBuilder();
		KmlFormatter.start(kml);
		KmlFormatter.format(kml, location);
		KmlFormatter.formatPointsOnly(kml, array, "");
		KmlFormatter.end(kml);
		CircleTuple.log.info(kml.toString());
		CircleTuple.log.info( "Distance:" + radiusMeters + " " + distance);

        return factory_.createPolygon(factory_.createLinearRing(array), null);
	}

	/**
	 * 
	 */
	public Tuple getCriterion()
	{
		Polygon range = makeCircle(center_, distanceInMeters_);
		StringBuilder builder = new StringBuilder();
		if ( getAlias() != null ) {
			
			builder.append(" within(");
			builder.append(getAlias().getSimpleName());
			builder.append( ". ");
			builder.append(getField());
			builder.append(", :circle)= true ");
				
		} else {
			
			builder.append(" within(");
			builder.append(getField());
			builder.append(", :circle)= true ");
			
		}
		
		Tuple rtn = new Tuple(builder.toString());
		rtn.add("circle", range);
		return rtn;
		
	}
	
	public void getDoucmentQuery(BasicDBObject query) {
				
        Coordinate coord = this.center_.getCoordinate();                
        query.append(getField(), new BasicDBObject("$geoWithin", 
                                                   new Object[]{ new Double[]{ coord.x , coord.y }, 
                                                   this.distanceInMeters_  }));
	}


}

