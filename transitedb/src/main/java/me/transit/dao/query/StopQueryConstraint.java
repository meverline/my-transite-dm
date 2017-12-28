package me.transit.dao.query;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Agency;
import me.transit.database.TransitStop;


public class StopQueryConstraint extends SpatialQuery {

	/**
	 * 
	 */
	public StopQueryConstraint() {
		super(TransitStop.class);
		
		addOrderBy( TransitStop.STOP_NAME);
	}
	
	/**
	 * 
	 * @param location
	 * @param distanceInMeters
	 */
	public void addCircleConstriant(Point location, double distanceInMeters)
	{
		this.addCircle(TransitStop.LOCATION, location, distanceInMeters);
	}
	
	/**
	 * 
	 * @param ll
	 * @param ur
	 */
	public void addRectangleConstraint(Point ll, Point ur)
	{
		this.addRectangle(TransitStop.LOCATION, ll, ur);
	}
	
	/**
	 * 
	 * @param aList
	 */
	public void addPolygonConstraint(List<Point> aList)
	{
		this.addPolygon(TransitStop.LOCATION, aList);
	}
	
	/**;
	 * 
	 * @param aList
	 */
	public void addAgency(Agency who)
	{
		if ( who.getUUID() != -1 ) {
			add ( new StringTuple( Agency.class, "name", who.getName(), StringTuple.MATCH.EXACT ));
		}
	}
		
}
