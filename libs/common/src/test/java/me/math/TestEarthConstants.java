package me.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEarthConstants {

	@Test
	public void test() {
		
		double rad = EarthConstants.toRadians(45.0);
		assertEquals(0.7853981633974483, rad, 0.1);	
		
		assertEquals(45.0, EarthConstants.toDegress(rad), 0.1);		
		assertEquals(6378137.0, EarthConstants.EquatorialRadiusMeters(), 0.1);
		
		Vertex lhs = new Vertex(40, 10);
		Vertex rhs = new Vertex(10, 40);
	
		assertEquals(4463587.816537507, EarthConstants.distanceMeters(lhs, rhs), 0.01);
	}

}
