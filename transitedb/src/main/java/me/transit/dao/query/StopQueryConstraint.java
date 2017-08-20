package me.transit.dao.query;

import java.util.List;

import me.factory.DaoBeanFactory;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Agency;
import me.transit.database.TransitStop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


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
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log log = LogFactory.getLog(StopQueryConstraint.class);
		
		TransiteStopDao dao =
			TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));
		
		GeometryFactory factory = new GeometryFactory();
		StopQueryConstraint query = new StopQueryConstraint();
		
		Point ur = factory.createPoint( new Coordinate(-77.095, 38.89));
		Point ll = factory.createPoint( new Coordinate(-77.078, 38.871));
		
		 query.addRectangleConstraint(ur, ll);
		 List<TransitStop> stops = dao.query(query);
		 
		 for ( TransitStop st : stops) {
			 log.info(st.getId() + " " + st.getName());			 
		 }
		 log.info("done query");
	
	}
	
}
