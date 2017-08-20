package me.crime.database;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Point;


public class GeoPointTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		GeoPoint pt = new GeoPoint();
		
		assertEquals(0.0, pt.getLatX(), 0.01);
		assertEquals(0.0, pt.getLonY(), 0.01);
		assertEquals(0, pt.getSRID());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		GeoPoint pt = new GeoPoint();
		
		pt.setLatX(35.0);
		pt.setLonY(-78.0);
		pt.setSRID(287);
		
		assertEquals(35.0, pt.getLatX(), 0.01);
		assertEquals(-78.0, pt.getLonY(), 0.01);
		assertEquals(287, pt.getSRID());
		
		Point loc = pt.asPoint();
		
		assertEquals(35.0, loc.getCoordinate().x, 0.01);
		assertEquals(-78.0, loc.getCoordinate().y, 0.01);		
	}

}
