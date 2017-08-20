package transit.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.Trip;
import me.transit.database.impl.AgencyImpl;
import me.transit.database.impl.RouteImpl;

import org.junit.Test;

public class RouteImplTest {

	@Test
	public void testConstructor() {
		Route route = new RouteImpl();
		
		assertNull(route.getAgency());
		assertNull(route.getId());
		assertEquals(-1, route.getUUID());
		assertEquals("0.5", route.getVersion());

		assertEquals("", route.getColor());
		assertEquals("", route.getDesc());
		assertEquals("", route.getLongName());
		assertEquals("", route.getShortName());
		assertEquals("", route.getTextColor());
		assertEquals("", route.getUrl());
		assertEquals( Route.RouteType.UNKOWN, route.getType());
		assertNotNull( route.getTripList());

	}
	
	@Test
	public void testGetAndSet() {
		Agency agency = new AgencyImpl("name");
		Route route = new RouteImpl();
		
		route.setAgency(agency);
		route.setId("Id");
		route.setUUID(10);
		route.setVersion("1.0");
		route.setColor("blue");
		route.setDesc("blue line");
		route.setLongName("blue line is this");
		route.setShortName("bl");
		route.setTextColor("black");
		route.setUrl("URL");
		route.setType(Route.RouteType.FUNICULAR);
		
		
		for ( int ndx = 0; ndx < 5; ndx++ ) {
			Trip trip = TripImplTest.createTrip();
			route.getTripList().add(trip);
		}
		
		assertEquals(agency, route.getAgency());
		assertEquals("Id", route.getId());
		assertEquals(10, route.getUUID());
		assertEquals("1.0", route.getVersion());

		assertEquals("blue", route.getColor());
		assertEquals("blue line", route.getDesc());
		assertEquals("blue line is this", route.getLongName());
		assertEquals("bl", route.getShortName());
		assertEquals("black", route.getTextColor());
		assertEquals("URL", route.getUrl());
		assertEquals( Route.RouteType.FUNICULAR, route.getType());
		assertNotNull( route.getTripList());
		
		assertEquals(5, route.getTripList().size());
		
		Trip tmp = TripImplTest.createTrip();
		for ( int ndx=0; ndx < route.getTripList().size(); ndx++ ) {
			Trip trip = route.getTripList().get(ndx);
			
			assertEquals(tmp.getDirectionId(), trip.getDirectionId());
			assertEquals(tmp.getHeadSign(), trip.getHeadSign());
		}
		
	}

}
