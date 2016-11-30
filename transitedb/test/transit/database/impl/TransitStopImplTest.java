package transit.database.impl;

import static org.junit.Assert.*;
import me.transit.database.Agency;
import me.transit.database.TransitStop;
import me.transit.database.impl.AgencyImpl;
import me.transit.database.impl.TransitStopImpl;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class TransitStopImplTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		TransitStop stop = new TransitStopImpl();
		
		assertNull(stop.getAgency());
		assertEquals(-1, stop.getUUID());
		assertNull(stop.getId());
		assertEquals("0.5", stop.getVersion());
		assertNull(stop.getZoneId());
		assertNull(stop.getLocation());
		assertEquals(TransitStop.LocationType.UNKNOW, stop.getLocationType());
		assertEquals("", stop.getCode());
		assertEquals("", stop.getDesc());
		assertNull(stop.getName());
		assertEquals(-1, stop.getParentStation());
		assertEquals("", stop.getUrl());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		GeometryFactory factory = new GeometryFactory();
		Agency agency = new AgencyImpl("name");
		TransitStop stop = new TransitStopImpl();
		
		stop.setAgency(agency);
		stop.setUUID(10);
		stop.setId("Id");
		stop.setVersion("1.0");
		
		stop.setZoneId("zone");
		stop.setCode("Code");
		stop.setDesc("Description");
		stop.setName("name");
		stop.setParentStation(10);
		stop.setUrl("Url");
		stop.setLocationType(TransitStop.LocationType.STOP);
		
		Point pt = factory.createPoint(new Coordinate(10, 20));
		stop.setLocation(pt);
		
		assertEquals(agency, stop.getAgency());
		assertEquals(10, stop.getUUID());
		assertEquals("Id", stop.getId());
		assertEquals("1.0", stop.getVersion());
		assertEquals("zone", stop.getZoneId());
		assertEquals(pt, stop.getLocation());
		assertEquals(TransitStop.LocationType.STOP, stop.getLocationType());
		assertEquals("Code", stop.getCode());
		assertEquals("Description", stop.getDesc());
		assertEquals("name", stop.getName());
		assertEquals(10, stop.getParentStation());
		assertEquals("Url", stop.getUrl());
	}
	
	

}
