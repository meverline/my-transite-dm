package me.math.kdtree.search;

import static org.junit.Assert.*;

import org.junit.Test;

import me.math.Vertex;
import me.utils.TransiteEnums;

public class TestRangeSearch {

	private Vertex ul = new Vertex(38.941, -77.286);
	private Vertex lr = new Vertex(38.827, -77.078);
	
	@Test
	public void testConstructor() {
		
		RangeSearch obj = new RangeSearch(ul, 300);
		
		assertEquals(300, obj.getDistanceInMeters(), 0.01);
		assertEquals(ul.getLatitudeDegress(), obj.getPoint().getLatitudeDegress(), 0.001);
		assertEquals(ul.getLongitudeDegress(), obj.getPoint().getLongitudeDegress(), 0.001);
		
		obj = new RangeSearch(ul.toPoint(), 300);
		assertEquals(ul.getLatitudeDegress(), obj.getVertex().getLatitudeDegress(), 0.001);
		assertEquals(ul.getLongitudeDegress(), obj.getVertex().getLongitudeDegress(), 0.001);
		
		obj.setPoint(lr);
		assertEquals(lr.getLatitudeDegress(), obj.getPoint().getLatitudeDegress(), 0.001);
		assertEquals(lr.getLongitudeDegress(), obj.getPoint().getLongitudeDegress(), 0.001);
		
		obj.setPoint(lr.toPoint());
		assertEquals(lr.getLatitudeDegress(), obj.getVertex().getLatitudeDegress(), 0.001);
		assertEquals(lr.getLongitudeDegress(), obj.getVertex().getLongitudeDegress(), 0.001);

		assertTrue(obj.getList().isEmpty());
		
	}

}
