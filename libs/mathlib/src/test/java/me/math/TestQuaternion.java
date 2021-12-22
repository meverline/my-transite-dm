 package me.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestQuaternion {
	
	Quaternion a;
	Quaternion b;
	VectorMath vec;
	
	@Before
	public void setUp() {
		a = new Quaternion(3.0, 1.0, 0.0, 0.0);
		b = new Quaternion(0.0, 5.0, 1.0, -2.0);
		vec = new VectorMath(2,3,4);
	}

	@Test
	public void test() {
		Quaternion a = new Quaternion(3.0, 1.0, 0.0, 0.0);

	    assertEquals(3.0, a.getX0(), 0.1);
	    assertEquals(1.0, a.getX1(), 0.1);
	    assertEquals(0.0, a.getX2(), 0.1);
	    assertEquals(0.0, a.getX3(), 0.1);

		Quaternion b = new Quaternion(0.0, 5.0, 1.0, -2.0);
		
		assertEquals(3.1622776601683795, a.norm(), 0.01);
		assertEquals("Quaternion(x0=3.0, x1=-1.0, x2=-0.0, x3=-0.0)", a.conjugate().toString());
		assertEquals("Quaternion(x0=3.0, x1=6.0, x2=1.0, x3=-2.0)", a.plus(b).toString());
		assertEquals("Quaternion(x0=-5.0, x1=15.0, x2=5.0, x3=-5.0)", a.times(b).toString());
		assertEquals("Quaternion(x0=-5.0, x1=15.0, x2=1.0, x3=-7.0)", b.times(a).toString());
		
		assertEquals("Quaternion(x0=0.5, x1=1.5, x2=0.09999999999999998, x3=-0.7)", a.divides(b).toString());
		assertEquals("Quaternion(x0=0.3, x1=-0.1, x2=-0.0, x3=-0.0)", a.inverse().toString());
		assertEquals("Quaternion(x0=0.9999999999999999, x1=-5.551115123125783E-17, x2=0.0, x3=0.0)", a.inverse().times(a).toString());
		assertEquals("Quaternion(x0=0.9999999999999999, x1=-5.551115123125783E-17, x2=0.0, x3=0.0)", a.times(a.inverse()).toString());
	}
	
	@Test
	public void testSetGets() {
			    
	    a.setX0(10.0);
	    a.setX1(20.0);
	    a.setX2(30.0);
	    a.setX3(40.0);
	    
	    assertEquals(10.0, a.getX0(), 0.1);
	    assertEquals(20.0, a.getX1(), 0.1);
	    assertEquals(30.0, a.getX2(), 0.1);
	    assertEquals(40.0, a.getX3(), 0.1);
	    	  
	}
	
	@Test
	public void testVectorConstruct() {
	    
	    Quaternion av = new Quaternion(1.0, vec);
	    
	    assertEquals(1.0, av.getX0(), 0.1);
	    assertEquals(2.0, av.getX1(), 0.1);
	    assertEquals(3.0, av.getX2(), 0.1);
	    assertEquals(4.0, av.getX3(), 0.1);
	    
	    vec = av.vectorPart();
	    assertEquals(2.0, vec.getX(), 1.0);
	    assertEquals(3.0, vec.getY(), 1.0);
	    assertEquals(4.0, vec.getZ(), 1.0);
	    
	}
	
	@Test
	public void testNormilize() {
		Quaternion c = b.normalize();
		
		assertEquals(0.0, c.getX0(), 0.1);
	    assertEquals(0.9128709291752769, c.getX1(), 0.1);
	    assertEquals(0.18257418583505536, c.getX2(), 0.1);
	    assertEquals(-0.3651483716701107, c.getX3(), 0.1);
	}
	
	@Test
	public void testConjugate() {
		Quaternion c = Quaternion.conjugate(a);
		
		assertEquals("Quaternion(x0=3.0, x1=-1.0, x2=-0.0, x3=-0.0)", c.toString());
	}
	
	@Test
	public void testRotation() {
		Quaternion c = Quaternion.rotationX(45.0);
		assertEquals("Quaternion(x0=-0.8733046400935156, x1=-0.4871745124605095, x2=-0.0, x3=-0.0)", c.toString());
		c = Quaternion.rotationY(45.0);
		assertEquals("Quaternion(x0=-0.8733046400935156, x1=-0.0, x2=-0.4871745124605095, x3=-0.0)", c.toString());
		c = Quaternion.rotationZ(45.0);
		assertEquals("Quaternion(x0=-0.8733046400935156, x1=-0.0, x2=-0.0, x3=-0.4871745124605095)", c.toString());
		
		c = Quaternion.rotationAxis(vec, vec, 45);
		assertEquals("Quaternion(x0=-0.8733046400935156, x1=NaN, x2=NaN, x3=NaN)", c.toString());
		System.out.println(c.toString());
		VectorMath vc = Quaternion.rotate(a, vec);
		assertEquals("VectorMath(20.0, 0.0, 50.0)", vc.toString());
	
		assertEquals("Quaternion(x0=0.9999999999999999, x1=-5.551115123125783E-17, x2=0.0, x3=0.0)", a.times(a.inverse()).toString());
		
	}


}
