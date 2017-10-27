package me.math.grid;

import static org.junit.Assert.*;

import org.junit.Test;
import org.meanbean.test.BeanTester;

public class TestSpatialGridData {

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		tester.testBean(SpatialGridDataMock.class);
	}

	private static  class SpatialGridDataMock extends SpatialGridData {

		@Override
		public double getValue() {
			// TODO Auto-generated method stub
			return 10;
		}
		
	}
}
