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

import java.util.ArrayList;
import java.util.List;

import me.output.KmlFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernatespatial.Circle;
import org.hibernatespatial.criterion.SpatialRestrictions;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

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
	private Polygon makeCircle( Point location, double radiusMeters ) {

		double distance = radiusMeters / CircleTuple.equatorialRadiusInMeters();
		Circle circle = new Circle(location.getCoordinate(), distance);
		
		List<Coordinate> coords = new ArrayList<Coordinate>();
		for ( int ndx = 0; ndx < 360; ndx += 5 ) {		
			coords.add( circle.getPoint(Math.toRadians(ndx)));
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
	public void getCriterion(Criteria crit)
	{
		Polygon range = makeCircle(center_, distanceInMeters_);

		if ( getAlias() != null ) {
			String name =  getAlias().getSimpleName();
			crit.createAlias( name, name);
			
			StringBuilder builder = new StringBuilder(name);
			builder.append(".");
			builder.append(getField());
			crit.add(SpatialRestrictions.filter( builder.toString(), range));
			crit.add(SpatialRestrictions.within( builder.toString(), range));
			
		} else {
			
			crit.add(SpatialRestrictions.filter( getField(), range));
			crit.add(SpatialRestrictions.within( getField(), range));
		}
		
	}

}

