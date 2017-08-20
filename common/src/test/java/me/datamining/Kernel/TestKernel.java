package me.datamining.Kernel;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestKernel {

	@Test
	public void testBiweight() {
		
		Biweight bi = new Biweight();
		
		double kvalues[] = { 1, 0.5, 0.6, 0.7, 0.8 };
		
		for ( double k : kvalues) {
			bi.kernelValue(k);
		}
		
	}
	
	@Test
	public void testGaussian() {
		
		Gaussian bi = new Gaussian();
		
		double kvalues[] = { 1, 0.5, 0.6, 0.7, 0.8 };
		
		for ( double k : kvalues) {
			bi.kernelValue(k);
		}
		
	}

	
	@Test
	public void testEpanechnikov() {
		
		Epanechnikov bi = new Epanechnikov();
		
		System.out.print(java.lang.Math.sqrt(5.0));
		
		double kvalues[] = { 5, 0.5, 0.6, 0.7, 0.8 };
		
		for ( double k : kvalues) {
			bi.kernelValue(k);
		}
		
	}
}
