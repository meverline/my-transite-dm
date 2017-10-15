package me.math;

import static org.junit.Assert.*;

<<<<<<< HEAD
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
		
=======
import org.junit.Test;

public class TestQuaternion {

	@Test
	public void test() {
		Quaternion a = new Quaternion(3.0, 1.0, 0.0, 0.0);
>>>>>>> master
	    assertEquals(3.0, a.getX0(), 0.1);
	    assertEquals(1.0, a.getX1(), 0.1);
	    assertEquals(0.0, a.getX2(), 0.1);
	    assertEquals(0.0, a.getX3(), 0.1);
<<<<<<< HEAD
	    		
=======
	    
		Quaternion b = new Quaternion(0.0, 5.0, 1.0, -2.0);
		
>>>>>>> master
		assertEquals(3.1622776601683795, a.norm(), 0.01);
		assertEquals("3.0 + -1.0i + -0.0j + -0.0k", a.conjugate().toString());
		assertEquals("3.0 + 6.0i + 1.0j + -2.0k", a.plus(b).toString());
		assertEquals("-5.0 + 15.0i + 5.0j + -5.0k", a.times(b).toString());
		assertEquals("-5.0 + 15.0i + 1.0j + -7.0k", b.times(a).toString());
		
		assertEquals("0.5 + 1.5i + 0.09999999999999998j + -0.7k", a.divides(b).toString());
		assertEquals("0.3 + -0.1i + -0.0j + -0.0k", a.inverse().toString());
		assertEquals("0.9999999999999999 + -5.551115123125783E-17i + 0.0j + 0.0k", a.inverse().times(a).toString());
<<<<<<< HEAD
		assertEquals("0.9999999999999999 + -5.551115123125783E-17i + 0.0j + 0.0k", a.times(a.inverse()).toString());	
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
		
		assertEquals("3.0 + -1.0i + -0.0j + -0.0k", c.toString());
	}
	
	@Test
	public void testRotation() {
		Quaternion c = Quaternion.rotationX(45.0);
		assertEquals("-0.8733046400935156 + -0.4871745124605095i + -0.0j + -0.0k", c.toString());
		c = Quaternion.rotationY(45.0);
		assertEquals("-0.8733046400935156 + -0.0i + -0.4871745124605095j + -0.0k", c.toString());
		c = Quaternion.rotationZ(45.0);
		assertEquals("-0.8733046400935156 + -0.0i + -0.0j + -0.4871745124605095k", c.toString());
		
		c = Quaternion.rotationAxis(vec, vec, 45);
		assertEquals("-0.8733046400935156 + NaNi + NaNj + NaNk", c.toString());
		System.out.println(c.toString());
		VectorMath vc = Quaternion.rotate(a, vec);
		assertEquals("<20.0, 0.0, 50.0>", vc.toString());
	}
	

=======
		assertEquals("0.9999999999999999 + -5.551115123125783E-17i + 0.0j + 0.0k", a.times(a.inverse()).toString());
		
	}
>>>>>>> master

}
