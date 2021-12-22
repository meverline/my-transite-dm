package me.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestThreeRotationQuaternion {

	@Test
	public void testRotationXYZ() {
		ThreeRotationQuaternion obj = ThreeRotationQuaternion.RotationXYZ(10, 5, 5);
		
		assertEquals(10, obj.getAngleA(), 0.01);
		assertEquals(5.0, obj.getAngleB(), 0.01);
		assertEquals(5.0, obj.getAngleC(), 0.01);
		
		assertEquals(0.52552007, obj.getX0(), 0.01);
		assertEquals(-0.51386844005, obj.getX1(), 0.01);
		assertEquals(-0.595773159, obj.getX2(), 0.01);
	}
	
	@Test
	public void testRotationYXY() {
		ThreeRotationQuaternion obj = ThreeRotationQuaternion.RotationYXY(10, 5, 5);
		
		assertEquals(10, obj.getAngleA(), 0.01);
		assertEquals(5.0, obj.getAngleB(), 0.01);
		assertEquals(5.0, obj.getAngleC(), 0.01);
		
		assertEquals(-0.2777046718066131, obj.getX0(), 0.01);
		assertEquals(-0.47946213733156917, obj.getX1(), 0.01);
		assertEquals(-0.7514726927762542, obj.getX2(), 0.01);
	}
	
	@Test
	public void testRotationZYX() {
		ThreeRotationQuaternion obj = ThreeRotationQuaternion.RotationZYX(10, 5, 5);
		
		assertEquals(10, obj.getAngleA(), 0.01);
		assertEquals(5.0, obj.getAngleB(), 0.01);
		assertEquals(5.0, obj.getAngleC(), 0.01);
				
		assertEquals(-0.16139364914672671, obj.getX0(), 0.01);
		assertEquals(-0.5957731599914555, obj.getX1(), 0.01);
		assertEquals(0.32376260454677064, obj.getX2(), 0.01);
	}


}
