package transit.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import me.transit.database.Agency;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.StopTime;
import me.transit.database.Trip;

public class TripTest {

	@Test
	public void testConstructor() {
		Trip trip = new Trip();
		
		assertNull(trip.getAgency());
		assertNull(trip.getId());
		assertNull(trip.getShape());
		assertEquals(-1, trip.getUUID());
		assertEquals("0.5", trip.getVersion());
		assertEquals("",trip.getHeadSign());
		assertNull(trip.getService());
		assertEquals("",trip.getShortName());
		assertNotNull(trip.getStopTimes());
		assertEquals(Trip.DirectionType.UNKOWN, trip.getDirectionId());
	}
	
	/**
	 * 
	 */
	public static RouteGeometry createGeometry()
	{
		GeometryFactory factory = new GeometryFactory();
		Agency agency = new Agency();
		RouteGeometry geo = new RouteGeometry();
		
		geo.setAgency(agency);
		geo.setId("id");
		geo.setUUID(10);
		geo.setVersion("1.0");
		
		Coordinate array[] = new Coordinate[2];
		array[0] = new Coordinate(10,10);
		array[1] = new Coordinate(20,02);
		
		LineString poly =  factory.createLineString(array);
		geo.setShape(poly);
		return geo;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Trip createTrip()
	{
		Agency agency = new Agency("name");
		Trip trip = new Trip();
		RouteGeometry geo = TripTest.createGeometry();
		ServiceDate service = ServiceDateTest.createServiceDate();
		
		trip.setAgency(agency);
		trip.setId("Id");
		trip.setUUID(10);
		trip.setVersion("1.0");
		trip.setShape(geo);
		trip.setHeadSign("headSign");
		trip.setShortName("shortName");
		trip.setDirectionId(Trip.DirectionType.IN_BOUND);
		trip.setService(service);
		
		for ( int ndx = 0; ndx < 5; ndx++ ) {
			StopTime st = StopTimeTest.createStopTime();
			trip.addStopTime(st);
		}
		return trip;
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		Agency agency = new Agency("name");
		Trip trip = new Trip();
		RouteGeometry geo = TripTest.createGeometry();
		ServiceDate service = ServiceDateTest.createServiceDate();
		
		trip.setAgency(agency);
		trip.setId("Id");
		trip.setUUID(10);
		trip.setVersion("1.0");
		trip.setShape(geo);
		trip.setHeadSign("headSign");
		trip.setShortName("shortName");
		trip.setDirectionId(Trip.DirectionType.IN_BOUND);
		trip.setService(service);
		
		for ( int ndx = 0; ndx < 5; ndx++ ) {
			StopTime st = StopTimeTest.createStopTime();
			trip.addStopTime(st);
		}
		
		assertEquals(agency, trip.getAgency());
		assertEquals("Id", trip.getId());
		assertEquals(geo, trip.getShape());
		assertEquals(10, trip.getUUID());
		assertEquals("1.0", trip.getVersion());
		assertEquals("headSign", trip.getHeadSign());
		assertEquals(service, trip.getService());
		assertEquals("shortName", trip.getShortName());
		assertEquals(Trip.DirectionType.IN_BOUND, trip.getDirectionId());
		
		assertEquals(5, trip.getStopTimes().size());
		
		StopTime tmp = StopTimeTest.createStopTime();
		for ( int ndx = 0; ndx < trip.getStopTimes().size(); ndx++ ) {
			StopTime st = trip.getStopTimes().get(ndx);
			
			assertEquals( tmp.getStopId(), st.getStopId());
		}
		
	}


}
