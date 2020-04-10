package me.math.grid.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDataSamples {

	@Test
	public void testDensityEstimateDataSample() {
		DensityEstimateDataSample obj = new DensityEstimateDataSample();
		
		assertEquals(0, obj.getValue(), 0.01);
		for ( int ndx = 1; ndx < 11; ndx++) {
			obj.addValue(ndx);
		}
		assertEquals(55.0, obj.getValue(), 0.01);
		
		obj.setInterpolationValue(0.15);
		assertEquals(0.15, obj.getInterpolationValue(), 0.01);
		
	}
	
	@Test 
	public void testSTINGDataSample() {
		STINGDataSample obj = new STINGDataSample();
		
		assertTrue(obj.isDataListEmpty());
		assertFalse(obj.isChecked());
		obj.setChecked(true);
		assertTrue(obj.isChecked());
		
		assertEquals(0, obj.getValue(), 0.01);
		
		for ( int ndx = 1; ndx < 11; ndx++) {
			obj.addValue(ndx);
		}
		
		assertFalse(obj.isDataListEmpty());
		assertEquals(10, obj.getSampleNumber(), 0.01);
		assertEquals(10, obj.getMax(), 0.01);
		assertEquals(1, obj.getMin(), 0.01);
		assertEquals(5.5, obj.average(), 0.01);
		assertEquals(3.0276503540, obj.standardDeviation(), 0.01);
		assertEquals(55, obj.getValue(), 0.01);
		
		obj.setInterpolationValue(0.15);
		assertEquals(0.15, obj.getInterpolationValue(), 0.01);

		
	}

}
