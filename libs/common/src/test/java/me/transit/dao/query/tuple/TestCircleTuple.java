package me.transit.dao.query.tuple;

import me.utils.TransiteEnums;
import org.easymock.EasyMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCircleTuple {

	private static final GeometryFactory factory_  = new GeometryFactory();

	@Test
	public void testConstructor() {

		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(1.1);
		Point ul = factory_.createPoint(new Coordinate(38.941, -77.286));

		CircleTuple obj = new CircleTuple("field", ul, distance);
		assertEquals(distance, obj.getDistanceInMeters(), 0.001);
		assertEquals(ul, obj.getCenter());
		assertEquals("field", obj.getField());

		obj = new CircleTuple(String.class, "field", ul, distance);

		assertEquals(distance, obj.getDistanceInMeters(), 0.001);
		assertEquals(ul, obj.getCenter());
		assertEquals("field", obj.getField());
		assertEquals(String.class, obj.getAlias());
		assertTrue(obj.hasMultipleCriterion());

	}

}
