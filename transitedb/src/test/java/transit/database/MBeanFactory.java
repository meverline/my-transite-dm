package transit.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.math.Vertex;
import me.math.kdtree.MinBoundingRectangle;
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

		protected final Vertex ul = new Vertex(38.941, -77.286);
		private final Agency agency = new Agency();

		public AgencyFactory() {
			agency.setFareUrl("https://www.coursera.org/learn/blockchain-basics?authMode=login");
			agency.setId("id");
			agency.setLang("lang");
			agency.setName("name");
			agency.setPhone("703-123-4567");
			agency.setTimezone("EST");
			agency.setUUID(100L);
			agency.setVersion("0.5");
			agency.setMBR((new MinBoundingRectangle(ul)).toPolygon());
		}

		@Override
		public Agency create() {
			return agency;
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
