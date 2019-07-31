package transit.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import me.transit.database.RouteStopData;

public class RouteStopDataImplTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		RouteStopData obj = new RouteStopData();

		assertNull(obj.getRouteShortName());
		assertNull(obj.getTripHeadSign());
	}

	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		RouteStopData obj = new RouteStopData();

		obj.setRouteShortName("ShortName");
		obj.setTripHeadSign("HeadSign");
		assertEquals("ShortName", obj.getRouteShortName());
		assertEquals("HeadSign", obj.getTripHeadSign());
	}

	/**
	 * 
	 */
	@Test
	public void testCsv() {
		RouteStopData lhs = new RouteStopData();

		lhs.setRouteShortName("ShortName");
		lhs.setTripHeadSign("HeadSign");

		String csv = lhs.toCSVLine();
		RouteStopData rhs = new RouteStopData();

		rhs.fromCSVLine(csv);

		assertEquals(lhs.getRouteShortName(), rhs.getRouteShortName());
		assertEquals(lhs.getTripHeadSign(), rhs.getTripHeadSign());
	}

}
