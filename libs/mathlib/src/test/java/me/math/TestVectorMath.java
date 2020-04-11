package me.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;

public class TestVectorMath {

	@Test
	public void testConstructor() {
		VectorMath vec = new VectorMath(10, 20, 30);
		
		assertEquals(10.0, vec.getX(), 0.01);
		assertEquals(20.0, vec.getY(), 0.01);
		assertEquals(30.0, vec.getZ(), 0.01);

		VectorMath unit =  vec.unit();
		
		vec = new VectorMath(true, unit.getX(), unit.getY(), unit.getZ());
		
		assertEquals(unit.getX(), vec.getX(), 0.01);
		assertEquals(unit.getY(), vec.getY(), 0.01);
		assertEquals(unit.getZ(), vec.getZ(), 0.01);

	}
	
	@Test
	public void testAdd() {
		VectorMath lhs = new VectorMath(10, 20, 30);
		VectorMath rhs = new VectorMath(10, 20, 30);
		
		VectorMath rtn = lhs.add(rhs);
		assertEquals(20.0, rtn.getX(), 0.01);
		assertEquals(40.0, rtn.getY(), 0.01);
		assertEquals(60.0, rtn.getZ(), 0.01);
		
		VectorMath[] vec = new VectorMath[5];
		Arrays.fill(vec, rhs);

		rtn = lhs.add(vec);
		assertEquals(10.0*6, rtn.getX(), 0.01);
		assertEquals(20.0*6, rtn.getY(), 0.01);
		assertEquals(30.0*6, rtn.getZ(), 0.01);
	
	}
	
	@Test
	public void testSubtract() {
		VectorMath lhs = new VectorMath(10, 20, 30);
		VectorMath rhs = new VectorMath(5, 5, 5);
		
		VectorMath rtn = lhs.subtract(rhs);
		assertEquals(5.0, rtn.getX(), 0.01);
		assertEquals(15.0, rtn.getY(), 0.01);
		assertEquals(25.0, rtn.getZ(), 0.01);
		
		rtn = lhs.minus(rhs);
		assertEquals(5.0, rtn.getX(), 0.01);
		assertEquals(15.0, rtn.getY(), 0.01);
		assertEquals(25.0, rtn.getZ(), 0.01);
		
		rtn = lhs.fromHereToThere(rhs);
		assertEquals(-5.0, rtn.getX(), 0.01);
		assertEquals(-15.0, rtn.getY(), 0.01);
		assertEquals(-25.0, rtn.getZ(), 0.01);

	}
	
	@Test
	public void testScalerMult() {
		VectorMath lhs = new VectorMath(10, 20, 30);
		VectorMath rhs = new VectorMath(5, 5, 5);
		
		assertEquals(0.3875966, lhs.angle(rhs), 0.001);
		
		rhs = new VectorMath(-5, -5, -5);
		
		assertEquals(0.3875966, lhs.angle(rhs), 0.001);
	
	}
	
	@Test
	public void testAngle() {
		VectorMath lhs = new VectorMath(10, 20, 30);
		
		VectorMath rtn = lhs.scalarMult(2);
		assertEquals(20.0, rtn.getX(), 0.01);
		assertEquals(40.0, rtn.getY(), 0.01);
		assertEquals(60.0, rtn.getZ(), 0.01);

	}
	
	@Test
	public void testDot() {
		VectorMath lhs = new VectorMath(10, 20, 30);
		VectorMath rhs = new VectorMath(5, 5, 5);
		
		assertEquals(300.0, lhs.dot(rhs), 0.01);

	}
	
	@Test
	public void testDistance() {
		VectorMath lhs = new VectorMath(10, 20, 30);
		VectorMath rhs = new VectorMath(5, 5, 5);
		
		assertEquals(29.58039891549, lhs.distance(rhs), 0.01);

	}
	
	@Test
	public void testNorm() {
		VectorMath lhs = new VectorMath(10, 20, 30);
		
		assertEquals(37.41657386773941, lhs.norm(), 0.01);
	}
	
}
