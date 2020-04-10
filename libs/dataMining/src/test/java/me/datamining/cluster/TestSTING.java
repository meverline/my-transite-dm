package me.datamining.cluster;

import static org.junit.Assert.*;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import me.datamining.cluster.STING.ClusterNodeEvaluation;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.grid.data.STINGDataSample;
import me.utils.TransiteEnums;

public class TestSTING {
	
	private Vertex ul = new Vertex(38.941, -77.286);
	private Vertex lr = new Vertex(38.827, -77.078);		
	private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
	private UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		tester.testBean(STING.class);
	}
	
	@Test
	public void testClusterNodeEvaluation() {
		
		ClusterNodeEvaluation testSubject = new ClusterNodeEvaluation(0, 100, 0.01);
		
		assertEquals(0, testSubject.getMin(), 0.01);
		assertEquals(100, testSubject.getMax(), 0.01);
		assertEquals(0.01, testSubject.getConfThresshold(), 0.01);
		
		assertEquals(0, testSubject.probablit(0.05, 5.0, 0), 0.01);
		assertEquals(2.5, testSubject.probablit(0.05, 5.0, 2), 0.01);
		
		assertFalse(testSubject.isRelevent(0.0, 0.0, 0.0));
		testSubject.isRelevent(0.5, 5.0, 20.0);
	}
	
	@Test
	public void testConstructors() {
		
		STING testSubject = new STING(grid);
		
		assertEquals( distance, testSubject.getGridSizeInMeters(), 0.01);
		
		testSubject = new STING(ul.toPoint(), lr.toPoint(), distance);
		assertEquals( distance, testSubject.getGridSizeInMeters(), 0.01);
		
		testSubject = new STING(100, 10, 0.4, 2);
		assertEquals( 100, testSubject.getRangeHi());
		assertEquals(10, testSubject.getRangeLow());
		assertEquals(0.4, testSubject.getConfidence(), 0.01);
		assertEquals(2, testSubject.getDensity(), 0.01);
	}
	
	@Test
	public void testStaticFunctions() {
		
		assertEquals(1.0, STING.standardPDF(50.0, 25.0, 0.25), 0.01);
		assertEquals(0, STING.standardPDF(50.0, 25.0, 0), 0.01);
		assertEquals(2.0200026568141E-95, STING.pissonPDF(50.0, 0.25), 0.01);
		assertEquals(0, STING.pissonPDF(50.0, 0), 0.01);
		assertEquals(7.8860905E-31, STING.binomialPDF(50, 25.0, 0.25), 0.01);
	}
	
	@Test
	public void testFindClusters() {
				
		for (AbstractSpatialGridPoint pt  : grid.getGridPoints()) {
			double value = Math.random()*10;
			if ( (int) value % 2 == 0) {
				pt.setData( new STINGDataSample());
				pt.getData().addValue(Math.random()*10);
				
				double cnt = Math.random()*10;
				for ( int ndx = 0; ndx < cnt; ndx++) {
					pt.getData().addValue(Math.random()*25);
				}
			}
		}
		
		STING testSubject = new STING();
		
		assertNotNull(testSubject.findClusters(grid));
	}
	
}
