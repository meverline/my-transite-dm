/**
 * 
 */
package me.transit.dao.query;

import java.util.List;

import me.transit.dao.query.tuple.CircleTuple;
import me.transit.dao.query.tuple.PolygonBoxTuple;
import me.transit.dao.query.tuple.RectangleTuple;

import org.locationtech.jts.geom.Point;

/**
 * @author markeverline
 *
 */
public class SpatialQuery extends QueryConstraints {

	/**
	 * 
	 * @param mainClass
	 */
	protected SpatialQuery(Class<?> mainClass) {
		super(mainClass);
	}
	
	/**
	 * 
	 * @param aClass
	 * @param field
	 * @param pt
	 * @param distanceMeters
	 */
	protected void addCircle(Class<?> aClass, String field, Point pt, double distanceMeters)
	{
		add( new CircleTuple(aClass, field, pt, distanceMeters));
	}
	
	/**
	 * 
	 * @param field
	 * @param pt
	 * @param distanceMeters
	 */
	protected void addCircle(String field, Point pt, double distanceMeters)
	{
		add( new CircleTuple(field, pt, distanceMeters));		
	}
	
	/**
	 * 
	 * @param aClass
	 * @param field
	 * @param ll
	 * @param ur
	 */
	protected void addRectangle(Class<?> aClass, String field, Point ll, Point ur)
	{
		add( new RectangleTuple(aClass, field, ll, ur));		
	}
	
	/**
	 * 
	 * @param field
	 * @param pt
	 * @param ll
	 * @param ur
	 */
	protected void addRectangle(String field, Point ll, Point ur)
	{
		add( new RectangleTuple( field, ll, ur));
	}
	
	/**
	 * 
	 * @param aClass
	 * @param field
	 * @param polygon
	 */
	protected void addPolygon(Class<?> aClass, String field, List<Point> polygon)
	{
		add( new PolygonBoxTuple(aClass, field, polygon));	
	}
	
	/**
	 * 
	 * @param field
	 * @param pt
	 * @param polygon
	 */
	protected void addPolygon(String field, List<Point> polygon)
	{
		add( new PolygonBoxTuple(field, polygon));	
	}
	
}
