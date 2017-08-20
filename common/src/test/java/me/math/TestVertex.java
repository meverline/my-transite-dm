package me.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Point;

import me.math.Vertex;

public class TestVertex {

	@Test
	public void testConstructor() {
		
		Vertex obj = new Vertex(38.941, -77.286);
		
		assertEquals( obj.getLatitudeDegress(), 38.941, 0.01);
		assertEquals( obj.getLongitudeDegress(), -77.286, 0.01);
	
		assertEquals( obj.getLatitudeRadians(), Math.toRadians(38.941), 0.01);
		assertEquals( obj.getLongitudeRadians(), Math.toRadians(-77.286), 0.01);
		
		assertNotNull(obj.toPoint());
		assertNotNull(obj.toString());
		
		obj.setLatitudeDegress(39.0);
		obj.setLongitudeDegress(50.0);
		assertEquals( obj.getLatitudeDegress(), 39.0, 0.01);
		assertEquals( obj.getLongitudeDegress(), 50.0, 0.01);
		
	}
	
	@Test
	public void testPointMethods() {
		Vertex obj = new Vertex(38.941, -77.286);
		Point pt = obj.toPoint();
		
		assertEquals(pt.getX(), 38.941, 0.01);
		assertEquals(pt.getY(), -77.286, 0.01);
		
		Vertex tmp = new Vertex(pt);
		assertEquals( tmp.getLatitudeDegress(), 38.941, 0.01);
		assertEquals( tmp.getLongitudeDegress(), -77.286, 0.01);
		
		Vertex fpt = new Vertex();
		fpt.formPoint(pt);
		assertEquals( fpt.getLatitudeDegress(), 38.941, 0.01);
		assertEquals( fpt.getLongitudeDegress(), -77.286, 0.01);
		
	}
	
	@Test
	public void testEquals() {
		Vertex obj = new Vertex(38.941, -77.286);
		Vertex two = new Vertex(obj);
		
		assertTrue(obj.equals(two));
		assertTrue(obj.equals(two));	

		Vertex item = new Vertex(38.837, -77.078);
		
		assertFalse(obj.equals(item));	
		
		String myString = "this is a string";
		assertFalse(obj.equals(myString));
	}
	
	@Test
	public void testDistanceFrom() {
		Vertex obj = new Vertex(38.941, -77.286);
		Vertex item = new Vertex(38.837, -77.078);
		assertEquals(21420.66694306803, obj.distanceFrom(item), 0.01);
	}
	
	@Test
	public void testGetEcfFromLatLon() {
		Vertex obj = new Vertex(38.941, -77.286);
		VectorMath two =  obj.getEcfFromLatLon();
		assertNotNull(two);
	
		assertEquals(1091811.8146583661, two.getX(), 0.01);
		assertEquals(-4839237.382444185, two.getY(), 0.01);
		assertEquals(4008785.3657306414, two.getZ(), 0.01);		
	}

}
