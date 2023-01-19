package me.transit.dao.query.tuple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class TestPolygonBoxTuple {

    private List<Point> box = new ArrayList<>();
    private static final GeometryFactory factory_  = new GeometryFactory();
    
    @Before
    public void setUp() {

		Coordinate [] coords = new Coordinate[5];

		coords[0] = new Coordinate(38.941, 77.078);
		coords[1] = new Coordinate(38.941, -77.286);
		coords[2] = new Coordinate(38.827, -77.286);
		coords[3] = new Coordinate(38.827, -77.078);
		coords[4] = coords[0];
		Polygon poly = factory_.createPolygon(factory_.createLinearRing(coords), null);

		
		for ( Coordinate cord : poly.getCoordinates()) {
			box.add( factory_.createPoint(cord));
		}
		
    }
    	
	@Test
	public void testConstructor() {
		PolygonBoxTuple obj = new PolygonBoxTuple("field", box);
		assertEquals("field", obj.getField());
		assertEquals(box, obj.getPointLine());

		obj = new PolygonBoxTuple(String.class, "field", box);

		assertEquals("field", obj.getField());
		assertEquals(box, obj.getPointLine());
		assertEquals(String.class, obj.getAlias());
		assertFalse(obj.hasMultipleCriterion());
			
	}

}
