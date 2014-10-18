package me.crime.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import me.math.Vertex;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class AddressTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		Address address = new Address();
		
		assertEquals(0, address.getId());
		assertEquals(0, address.getAccuracy());
		assertEquals("", address.getCity());
		assertEquals("", address.getLocation());
		assertEquals("", address.getService());
		assertEquals("", address.getState());
		assertEquals("", address.getZipCode());
		assertNull(address.getPoint());
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static Address createAddress() 
	{
		Address address = new Address();
		
		address.setAccuracy(10);
		address.setCity("Arlington");
		address.setService("goggle");
		address.setLocation("231 Laceyville Terrace.");
		address.setState("TX");
		address.setZipCode("99001");
		
		GeometryFactory factory = new GeometryFactory();
		Point pt = factory.createPoint(new Coordinate(-78.0, -35.0));
		address.setPoint(pt);
		return address;
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		Address address = new Address();
		
		address.setAccuracy(10);
		address.setCity("Arlington");
		address.setService("goggle");
		address.setLocation("231 Laceyville Terrace.");
		address.setState("TX");
		address.setZipCode("99001");
		
		GeometryFactory factory = new GeometryFactory();
		Point pt = factory.createPoint(new Coordinate(-78.0, -35.0));
		address.setPoint(pt);
	
		assertEquals(10, address.getAccuracy());
		assertEquals("Arlington", address.getCity());
		assertEquals("231 Laceyville Terrace.", address.getLocation());
		assertEquals("goggle", address.getService());
		assertEquals("TX", address.getState());
		assertEquals("99001", address.getZipCode());
		assertEquals(pt, address.getPoint());	
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testCopy() {
		Address lhs = AddressTest.createAddress();
		Address rhs = new Address();
		
		rhs.setAddress(lhs);
		
		assertEquals(lhs.getAccuracy(), rhs.getAccuracy());
		assertEquals(lhs.getCity(), rhs.getCity());
		assertEquals(lhs.getLocation(), rhs.getLocation());
		assertEquals(lhs.getService(), rhs.getService());
		assertEquals(lhs.getState(), rhs.getState());
		assertEquals(lhs.getZipCode(), rhs.getZipCode());
		assertEquals(lhs.getPoint(), rhs.getPoint());
		assertEquals(lhs.getId(), rhs.getId());
	}
	
	/**
	 * 
	 */
	@Test
	public void testMethods() {
		Address lhs = AddressTest.createAddress();
		
		Vertex ver = lhs.getPointAsVertex();
		
		assertEquals(lhs.getPoint().getCoordinate().x, ver.getLatitudeDegress(), 0.01);
		assertEquals(lhs.getPoint().getCoordinate().y, ver.getLongitudeDegress(), 0.01);
		
		String tmp = "231 Laceyville Terrace.,Arlington TX 99001 ";
		
		assertEquals(tmp, lhs.getFullAddress());
		
		GeoPoint pt = new GeoPoint();
		
		pt.setLatX(55.0);
		pt.setLonY(-170.0);
		pt.setSRID(287);
		
		lhs.handleObject(pt);
		
		assertEquals(pt.getLatX(), lhs.getPoint().getCoordinate().x, 0.01);
		assertEquals(pt.getLonY(), lhs.getPoint().getCoordinate().y, 0.01);	
		
		lhs.setService("yahoo");
		
		pt.setLonY(55.0);
		pt.setLatX(-170.0);
		lhs.handleObject(pt);
		
		assertEquals(pt.getLatX(), lhs.getPoint().getCoordinate().x, 0.01);
		assertEquals(pt.getLonY(), lhs.getPoint().getCoordinate().y, 0.01);		
	}
}
