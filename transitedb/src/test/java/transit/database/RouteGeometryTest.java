package transit.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import me.transit.database.Agency;
import me.transit.database.RouteGeometry;

public class RouteGeometryTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testRouteGeometry() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("agency", new MBeanFactory.AgencyFactory())
																.overrideFactory("tripList", new MBeanFactory.TripListFactory())
																.overrideFactory("shape", new MBeanFactory.PoloygonFactory())
				.build();

		tester.testBean(RouteGeometry.class, configuration);
	}

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		RouteGeometry geo = new RouteGeometry();

		assertNull(geo.getAgency());
		assertNull(geo.getId());
		assertNull(geo.getShape());
		assertEquals(-1, geo.getUUID());
		assertEquals("0.5", geo.getVersion());
	}

	/**
	 * 
	 */
	public static RouteGeometry createGeometry() {
		GeometryFactory factory = new GeometryFactory();
		Agency agency = new Agency();
		RouteGeometry geo = new RouteGeometry();

		geo.setAgency(agency);
		geo.setId("id");
		geo.setUUID(10);
		geo.setVersion("1.0");

		Coordinate array[] = new Coordinate[2];
		array[0] = new Coordinate(10, 10);
		array[1] = new Coordinate(20, 02);

		LineString poly = factory.createLineString(array);
		geo.setShape(poly);
		return geo;
	}

	/**
	 * 
	 */
	@Test
	public void testGetAndSets() {
		GeometryFactory factory = new GeometryFactory();
		Agency agency = new Agency();
		RouteGeometry geo = RouteGeometryTest.createGeometry();

		geo.setAgency(agency);
		geo.setId("id");
		geo.setUUID(10);
		geo.setVersion("1.0");

		Coordinate array[] = new Coordinate[2];
		array[0] = new Coordinate(10, 10);
		array[1] = new Coordinate(20, 02);

		LineString poly = factory.createLineString(array);
		geo.setShape(poly);

		assertEquals(agency, geo.getAgency());
		assertEquals("id", geo.getId());
		assertEquals(poly, geo.getShape());
		assertEquals(10, geo.getUUID());
		assertEquals("1.0", geo.getVersion());
	}

}
