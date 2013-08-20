package me.transit.database.impl;

import static org.junit.Assert.*;

import java.util.Map;

import me.transit.database.StopTime;

import org.junit.Test;

public class StopTimeImplTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		
		StopTime st = new StopTimeImpl();
		
		assertNotNull(st.getArrivalTime());
		assertNotNull(st.getDepartureTime());
		assertEquals(StopTime.PickupType.UNKNOWN, st.getPickupType());
		assertEquals(StopTime.PickupType.UNKNOWN, st.getDropOffType());
		assertEquals("", st.getStopHeadSign());
		assertNull( st.getStopId());
		assertNull( st.getTripId());
		assertEquals(-1.0, st.getShapeDistTravel(), 0.01);
	}
	
	/**
	 * 
	 */
	public static StopTime createStopTime()
	{
		StopTime st = new StopTimeImpl();
		
		st.setStopHeadSign("Head Sign");
		st.setShapeDistTravel(10.0);
		st.setStopId("537");
		st.setTripId("523");
		st.setDropOffType(StopTime.PickupType.COORDINATE);
		st.setPickupType(StopTime.PickupType.PHONE);
		
		for ( int n = 0; n < 5; n++ ) {
			st.addArrivalTime(n);
		}
		
		for ( int n = 0; n < 5; n++ ) {
			st.addDepartureTime(n+10);
		}
		return st;
	}
	
	/**
	 * 
	 */
	@Test
	public void testSetGets() {
		StopTime st = StopTimeImplTest.createStopTime();
			
		assertNotNull(st.getArrivalTime());
		assertEquals(5, st.getArrivalTime().size());
		for (int n = 0; n < st.getArrivalTime().size(); n++ ) {
			assertEquals( n, st.getArrivalTime().get(n).intValue());
		}
		
		assertNotNull(st.getDepartureTime());
		assertEquals(5, st.getDepartureTime().size());
		for (int n = 0; n < st.getDepartureTime().size(); n++ ) {
			assertEquals( n+10, st.getDepartureTime().get(n).intValue());
		}
		
		assertEquals(StopTime.PickupType.PHONE, st.getPickupType());
		assertEquals(StopTime.PickupType.COORDINATE, st.getDropOffType());
		assertEquals("Head Sign", st.getStopHeadSign());
		assertEquals("537", st.getStopId());
		assertEquals("523", st.getTripId());
		assertEquals(10.0, st.getShapeDistTravel(), 0.01);
		
	}
	
	@Test
	public void testCSV() {
		StopTime lhs = StopTimeImplTest.createStopTime();
		
		String csv = lhs.toCSVLine();
		StopTime rhs = new StopTimeImpl();
		rhs.fromCSVLine(csv);
		
		assertEquals( lhs.getPickupType(), rhs.getPickupType());
		assertEquals( lhs.getDropOffType(), rhs.getDropOffType());
		assertEquals( lhs.getStopHeadSign(), rhs.getStopHeadSign());
		assertEquals( lhs.getStopId(), rhs.getStopId());
		assertEquals( lhs.getTripId(), rhs.getTripId());
		assertEquals( lhs.getShapeDistTravel(), rhs.getShapeDistTravel(), 0.01);
		
		
		assertNotNull(rhs.getArrivalTime());
		assertEquals(lhs.getArrivalTime().size(), rhs.getArrivalTime().size());
		for (int n = 0; n < rhs.getArrivalTime().size(); n++ ) {
			assertEquals( lhs.getArrivalTime().get(0).intValue(), rhs.getArrivalTime().get(0).intValue());
		}
		
		assertNotNull(rhs.getDepartureTime());
		assertEquals(lhs.getDepartureTime().size(), rhs.getDepartureTime().size());
		for (int n = 0; n < rhs.getDepartureTime().size(); n++ ) {
			assertEquals( lhs.getDepartureTime().get(0).intValue(), rhs.getDepartureTime().get(0).intValue());
		}
		
		Map<String, Object> map = rhs.toDocument();
		assertNotNull(map);
		
	}

}
