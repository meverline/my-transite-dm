package me.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestQuaternion {

	@Test
	public void test() {
		Quaternion a = new Quaternion(3.0, 1.0, 0.0, 0.0);
	    assertEquals(3.0, a.getX0(), 0.1);
	    assertEquals(1.0, a.getX1(), 0.1);
	    assertEquals(0.0, a.getX2(), 0.1);
	    assertEquals(0.0, a.getX3(), 0.1);
	    
		Quaternion b = new Quaternion(0.0, 5.0, 1.0, -2.0);
		
		assertEquals(3.1622776601683795, a.norm(), 0.01);
		assertEquals("3.0 + -1.0i + -0.0j + -0.0k", a.conjugate().toString());
		assertEquals("3.0 + 6.0i + 1.0j + -2.0k", a.plus(b).toString());
		assertEquals("-5.0 + 15.0i + 5.0j + -5.0k", a.times(b).toString());
		assertEquals("-5.0 + 15.0i + 1.0j + -7.0k", b.times(a).toString());
		
		assertEquals("0.5 + 1.5i + 0.09999999999999998j + -0.7k", a.divides(b).toString());
		assertEquals("0.3 + -0.1i + -0.0j + -0.0k", a.inverse().toString());
		assertEquals("0.9999999999999999 + -5.551115123125783E-17i + 0.0j + 0.0k", a.inverse().times(a).toString());
		assertEquals("0.9999999999999999 + -5.551115123125783E-17i + 0.0j + 0.0k", a.times(a.inverse()).toString());
		
	}

}
