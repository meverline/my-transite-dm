package me.math.grid;

import org.junit.Test;
import org.meanbean.test.BeanTester;

public class TestSpatialGridData {

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		tester.testBean(SpatialGridDataMock.class);
		
		@SuppressWarnings("unused")
		SpatialGridDataMock test = new SpatialGridDataMock(100);
	}

	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	private static  class SpatialGridDataMock extends SpatialGridData {
		
		@SuppressWarnings("unused")
		public SpatialGridDataMock() {
			
		}
		
		public SpatialGridDataMock(long gridPointIndex) {
			super(gridPointIndex);
		}

		@Override
		public double getValue() {
			return 10;
		}
		
	}
}
