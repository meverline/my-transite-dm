package me.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestECFPositionedFrame {

	@Test
	public void testConstructors() {
		ECFPositionedFrame obj = new ECFPositionedFrame();
		
		VectorMath vec = new VectorMath(38.9, -75.0, 0);
		ThreeRotationQuaternion tre = ThreeRotationQuaternion.RotationXYZ(10, 5, 5);
		
		obj = new ECFPositionedFrame(vec, tre);
		
		assertNotNull(obj.referenceFrame());
		assertNotNull(obj.transformToReferenceFrame());
		assertNotNull(obj.positionVertex());
		assertNotNull(obj.positionECF());
		assertNotNull(obj.compositeQuaternion());
		
		obj.refreshTransform();
	}
	
	@Test
	public void testPutECFVectorMathInLocalFrame() {
	
		VectorMath target = new VectorMath(20.5, -60.0, 0);
		VectorMath vec = new VectorMath(38.9, -75.0, 0);
		ThreeRotationQuaternion tre = ThreeRotationQuaternion.RotationXYZ(10, 5, 5);
		
		ECFPositionedFrame obj = new ECFPositionedFrame(vec, tre);
		
		VectorMath lf = obj.putECFVectorMathInLocalFrame(target);
		assertEquals(12.808241196855057, lf.getX(), 0.01);
		assertEquals(-1.0714841537502906, lf.getY(), 0.01);
		assertEquals(19.958979912596906, lf.getZ(), 0.01);
	}
	
	@Test
	public void testPutLocalFrameVectorMathIntoECF() {
	
		VectorMath target = new VectorMath(20.5, -60.0, 0);
		VectorMath vec = new VectorMath(38.9, -75.0, 0);
		ThreeRotationQuaternion tre = ThreeRotationQuaternion.RotationXYZ(10, 5, 5);
		
		ECFPositionedFrame obj = new ECFPositionedFrame(vec, tre);
		
		VectorMath lf = obj.putLocalFrameVectorMathIntoECF(target);
		
		assertEquals(24.228883500285267, lf.getX(), 0.01);
		assertEquals(-71.20603665561367, lf.getY(), 0.01);
		assertEquals(61.567963932496994, lf.getZ(), 0.01);
		
	}

}
