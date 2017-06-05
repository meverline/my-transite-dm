package me.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVertex {

	@Test
	public void test() {
		
		Vertex obj = new Vertex(38.941, -77.286);
		
		assertEquals( obj.getLatitudeDegress(), 38.941, 0.01);
		assertEquals( obj.getLongitudeDegress(), -77.286, 0.01);
	
		assertEquals( obj.getLatitudeRadians(), Math.toRadians(38.941), 0.01);
		assertEquals( obj.getLongitudeRadians(), Math.toRadians(-77.286), 0.01);
		
		assertNotNull(obj.getEcfFromLatLon());
		assertNotNull(obj.toPoint());
		assertNotNull(obj.toString());
		
		Vertex two = new Vertex(obj);
		
		assertTrue(obj.equals(two));

		Vertex item = new Vertex(38.837, -77.078);
		
		assertFalse(obj.equals(item));
		
		two = new Vertex( obj.getEcfFromLatLon());
		assertTrue(obj.equals(two));		
		assertEquals(21420.66694306803, obj.distanceFrom(item), 0.01);

	}

}
