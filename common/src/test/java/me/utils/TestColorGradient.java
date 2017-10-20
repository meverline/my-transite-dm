package me.utils;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Calendar;

import org.junit.Test;
import org.meanbean.lang.Factory;

public class TestColorGradient {

	@Test
	public void test() {
		ColorGradient obj = new ColorGradient(Color.BLUE, Color.LIGHT_GRAY, 30, 0.1, 0.9, 85);
		
		assertEquals(85, obj.getAlhpaValue());
		assertEquals(0.9, obj.getHigh(), 0.01);
		assertEquals(0.1, obj.getLow(), 0.01);
		
		assertTrue(obj.contains(0.5));
		assertTrue(! obj.contains(1.5));
		
		double values[] = { 1.0, 1.5, 0.5, 0.455, 0.3, 0.001 };
		for (double ndx : values) {
			assertNotNull(obj.findHeatMapColor(ndx));
		}
		
		
		obj.createColorGradiant(20, Color.red, Color.gray);
	}
	

}
