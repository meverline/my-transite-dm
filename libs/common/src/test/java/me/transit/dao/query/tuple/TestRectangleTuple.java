package me.transit.dao.query.tuple;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRectangleTuple {

	private static final GeometryFactory factory_  = new GeometryFactory();

	@Test
	public void testConstructor() {

		Point lr = factory_.createPoint(new Coordinate(38.827, -77.078));
		Point ul = factory_.createPoint(new Coordinate(38.941, -77.286));

		RectangleTuple obj = new RectangleTuple("field", ul, lr);

		assertEquals("field", obj.getField());
		assertEquals(ul, obj.getUl());
		assertEquals(lr, obj.getLr());

		obj = new RectangleTuple(String.class, "field", ul, lr);
		
		assertTrue(obj.hasMultipleCriterion());

		assertEquals("field", obj.getField());
		assertEquals(String.class, obj.getAlias());
		assertEquals(ul, obj.getUl());
		assertEquals(lr, obj.getLr());
			
	}

}
