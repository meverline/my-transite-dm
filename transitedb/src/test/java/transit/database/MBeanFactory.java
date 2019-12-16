package transit.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.meanbean.lang.Factory;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import me.transit.database.Agency;
import me.transit.database.Trip;

public class MBeanFactory {

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class PoloygonFactory implements Factory<Polygon> {

		private GeometryFactory factory = new GeometryFactory();

		@Override
		public Polygon create() {

			Coordinate coords[] = new Coordinate[5];
			for (int ndx = 0; ndx < 5; ndx++) {
				coords[ndx] = new Coordinate(ndx, ndx);
			}
			coords[4] = coords[0];
			return factory.createPolygon(coords);
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class PointFactory implements Factory<Point> {

		private GeometryFactory factory = new GeometryFactory();

		@Override
		public Point create() {
			return factory.createPoint(new Coordinate(2, 2));
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class CalendarFactory implements Factory<Calendar> {

		@Override
		public Calendar create() {
			return Calendar.getInstance();
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class AgencyFactory implements Factory<Agency> {

		@Override
		public Agency create() {
			return new Agency();
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class TripListFactory implements Factory<List<Trip>> {

		@Override
		public List<Trip> create() {
			return new ArrayList<Trip>();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class UserDataFactory implements Factory<Object> {

		@Override
		public Object create() {
			return new String();
		}
	}

}
