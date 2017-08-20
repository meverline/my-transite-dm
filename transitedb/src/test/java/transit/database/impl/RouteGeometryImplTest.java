package transit.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import me.transit.database.Agency;
import me.transit.database.RouteGeometry;
import me.transit.database.impl.AgencyImpl;
import me.transit.database.impl.RouteGeometryImpl;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class RouteGeometryImplTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		RouteGeometry geo = new RouteGeometryImpl();
		
		assertNull(geo.getAgency());
		assertNull(geo.getId());
		assertNull(geo.getShape());
		assertEquals(-1, geo.getUUID());
		assertEquals("0.5", geo.getVersion());
	}
	
	/**
	 * 
	 */
	public static RouteGeometry createGeometry()
	{
		GeometryFactory factory = new GeometryFactory();
		Agency agency = new AgencyImpl();
		RouteGeometry geo = new RouteGeometryImpl();
		
		geo.setAgency(agency);
		geo.setId("id");
		geo.setUUID(10);
		geo.setVersion("1.0");
		
		Coordinate array[] = new Coordinate[2];
		array[0] = new Coordinate(10,10);
		array[1] = new Coordinate(20,02);
		
		LineString poly =  factory.createLineString(array);
		geo.setShape(poly);
		return geo;
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSets() {
		GeometryFactory factory = new GeometryFactory();
		Agency agency = new AgencyImpl();
		RouteGeometry geo = RouteGeometryImplTest.createGeometry();
				
		geo.setAgency(agency);
		geo.setId("id");
		geo.setUUID(10);
		geo.setVersion("1.0");
		
		Coordinate array[] = new Coordinate[2];
		array[0] = new Coordinate(10,10);
		array[1] = new Coordinate(20,02);
		
		LineString poly =  factory.createLineString(array);
		geo.setShape(poly);
		
		assertEquals(agency, geo.getAgency());
		assertEquals("id", geo.getId());
		assertEquals(poly, geo.getShape());
		assertEquals(10, geo.getUUID());
		assertEquals("1.0", geo.getVersion());
	}

}
