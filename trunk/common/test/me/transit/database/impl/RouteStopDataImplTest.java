package me.transit.database.impl;

import static org.junit.Assert.*;
import me.transit.database.RouteStopData;

import org.junit.Test;

public class RouteStopDataImplTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		RouteStopData obj = new RouteStopDataImpl();
		
		assertNull(obj.getRouteShortName());
		assertNull(obj.getTripHeadSign());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		RouteStopData obj = new RouteStopDataImpl();
		
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
		RouteStopData lhs = new RouteStopDataImpl();
		
		lhs.setRouteShortName("ShortName");
		lhs.setTripHeadSign("HeadSign");
		
		String csv = lhs.toCSVLine();
		RouteStopData rhs = new RouteStopDataImpl();
		
		rhs.fromCSVLine(csv);
		
		assertEquals( lhs.getRouteShortName(), rhs.getRouteShortName());
		assertEquals( lhs.getTripHeadSign(), rhs.getTripHeadSign());
	}
	

}
