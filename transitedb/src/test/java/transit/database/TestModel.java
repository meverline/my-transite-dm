package transit.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import me.transit.database.Agency;
import me.transit.database.CalendarDate;
import me.transit.database.RouteGeometry;
import me.transit.database.RouteStopData;
import me.transit.database.ServiceDate;
import me.transit.database.TransitStop;
import me.transit.database.Route;
import me.transit.database.Trip;

public class TestModel {
	
	private BeanTester tester = new BeanTester();
	
	@Test
	public void testTrip() {
		tester.testBean(TripTest.class);
	}
	
	@Test
	public void testTransitStop() {
		
		Configuration configuration = new ConfigurationBuilder().
				overrideFactory("MBR", new PoloygonFactory()).
				overrideFactory("agency", new AgencyFactory()).
				overrideFactory("location", new PointFactory()).build();

		tester.testBean(TransitStop.class, configuration);
	}
	
	@Test
	public void testServiceDate() {
		
		Configuration configuration = new ConfigurationBuilder().
				overrideFactory("endDate", new CalendarFactory()).
				overrideFactory("startDate", new CalendarFactory()).
				overrideFactory("agency", new AgencyFactory()).build();
		
		tester.testBean(ServiceDate.class, configuration);
	}
	
	@Test
	public void testRouteStopData() {
		tester.testBean(RouteStopData.class);
	}
	
	@Test
	public void testRoute() {
		
		Configuration configuration = new ConfigurationBuilder().
				overrideFactory("MBR", new PoloygonFactory()).
				overrideFactory("agency", new AgencyFactory()).
				overrideFactory("location", new PointFactory()).build();

		tester.testBean(Route.class, configuration);
	}
	
	@Test
	public void testRouteGeometry() {	
		
		Configuration configuration = new ConfigurationBuilder().
				overrideFactory("agency", new AgencyFactory()).
				overrideFactory("tripList", new TripListFactory()).
				overrideFactory("shape", new PoloygonFactory()).build();

		tester.testBean(RouteGeometry.class, configuration);
	}
	
	@Test
	public void testCalendarDate() {
		
		Configuration configuration = new ConfigurationBuilder().
				overrideFactory("agency", new AgencyFactory()).
				overrideFactory("date", new CalendarFactory()).build();
		
		tester.testBean(CalendarDate.class, configuration);
	}
	
	@Test
	public void testAgency() {
		
		Configuration configuration = new ConfigurationBuilder().
				overrideFactory("MBR", new PoloygonFactory()).build();
		
		tester.testBean(Agency.class, configuration);
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	class PoloygonFactory implements Factory<Polygon> {
		
		private GeometryFactory factory = new GeometryFactory();
		
        @Override
        public Polygon create() {
        	
        	    Coordinate coords[] = new Coordinate[5];
        		for ( int ndx=0; ndx < 5; ndx++ ) {
        			coords[ndx] = new Coordinate(ndx, ndx);
        		}
        		coords[4] = coords[0];
            return factory.createPolygon(coords);
        }
    }
	
	class PointFactory implements Factory<Point> {
		
		private GeometryFactory factory = new GeometryFactory();
		
        @Override
        public Point create() {
            return factory.createPoint(new Coordinate(2,2));
        }
    }
	
	class CalendarFactory implements Factory<Calendar> {
		
        @Override
        public Calendar create() {
        	    return Calendar.getInstance();
        }
    }
	
	class AgencyFactory implements Factory<Agency> {
		
        @Override
        public Agency create() {
        	    return new Agency();
        }
    }
	
	class TripListFactory implements Factory<List<Trip>> {
		
        @Override
        public List<Trip> create() {
        	    return new ArrayList<Trip>();
        }
    }

}
