package transit.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import me.transit.database.Agency;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.StopTime;
import me.transit.database.Trip;
import me.transit.database.impl.AgencyImpl;
import me.transit.database.impl.RouteGeometryImpl;
import me.transit.database.impl.TripImpl;

import org.junit.Test;

public class TripImplTest {

	@Test
	public void testConstructor() {
		Trip trip = new TripImpl();
		
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
	 * @return
	 */
	public static Trip createTrip()
	{
		Agency agency = new AgencyImpl("name");
		Trip trip = new TripImpl();
		RouteGeometry geo = RouteGeometryImplTest.createGeometry();
		ServiceDate service = ServiceDateImplTest.createServiceDate();
		
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
			StopTime st = StopTimeImplTest.createStopTime();
			trip.addStopTime(st);
		}
		return trip;
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		Agency agency = new AgencyImpl("name");
		Trip trip = new TripImpl();
		RouteGeometry geo = RouteGeometryImplTest.createGeometry();
		ServiceDate service = ServiceDateImplTest.createServiceDate();
		
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
			StopTime st = StopTimeImplTest.createStopTime();
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
		
		StopTime tmp = StopTimeImplTest.createStopTime();
		for ( int ndx = 0; ndx < trip.getStopTimes().size(); ndx++ ) {
			StopTime st = trip.getStopTimes().get(ndx);
			
			assertEquals( tmp.getStopId(), st.getStopId());
		}
		
	}


}
