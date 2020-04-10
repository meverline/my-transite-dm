package me.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestOrthoFrame {

	@Test
	public void test() {
		
		OrthoFrame obj = new OrthoFrame();
		
		assertNotNull(obj.getXAxis());
		assertNotNull(obj.getYAxis());
		assertNotNull(obj.getZAxis());
		
		VectorMath vec = new VectorMath(10, 20, 30);
		obj.setXAxis(vec);
		assertEquals(vec, obj.getXAxis());
		
		obj.setYAxis(vec);
		assertEquals(vec, obj.getYAxis());
		
		obj.setZAxis(vec);
		assertEquals(vec, obj.getZAxis());
		
		obj = new OrthoFrame(vec, vec, vec);
		assertEquals(vec, obj.getXAxis());
		assertEquals(vec, obj.getYAxis());
		assertEquals(vec, obj.getZAxis());
	}
	
	@Test
	public void testTransform() {
		
		OrthoFrame obj = new OrthoFrame();
		Quaternion a = new Quaternion(3.0, 1.0, 0.0, 0.0);
		
		obj.applyTransform(a);
		obj.resetTransform();
		
		obj.resetTransform(a);
	}

}
