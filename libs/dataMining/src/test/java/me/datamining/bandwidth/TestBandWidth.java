package me.datamining.bandwidth;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Before;
import org.junit.Test;

public class TestBandWidth {

	private final static double data[] = { 0.056925519364541266, 0.05232580163075318, 0.30814914228698786, 0.2081442152043761,
			0.1224445981009083, 0.3210991945748876, 0.20160567851467315, 0.32343313930000644, 0.2902567509881666,
			0.4105216273741015, 0.1820160905304125, 0.2115909890217168, 0.4190192334994037, 0.4361769304774726,
			0.3884964187129246, 0.09206776921669818, 0.22229499301901268, 0.22313827489208682, 0.2522392230178504,
			0.3758891931688358, 0.2626101564916296, 0.0627739941175241, 0.22770052369889354, 0.3724462396766749,
			0.3895617251921008, 0.3636349443114718, 0.2512307512027196, 0.13500297570454034, 0.36173702707909844,
			0.16042804836829005, 0.49918682652523605, 0.42284289452007745, 0.31066314130745576, 0.1330686340544368,
			0.006331212839214673, 0.32877537368869975, 0.38908680618760766, 0.36641784232547026, 0.20971917813299246,
			0.33214911395615887, 0.3955923347732819, 0.05054940089379728, 0.0032941671206990764, 0.38957052351623844,
			0.38289670937440246, 0.1812532942917815, 0.42405657821724724, 0.1682007405361855, 0.2381187753954223,
			0.07097241938938348, };
	
	private DescriptiveStatistics stats;
	
	@Before
	public void setUp() {
		stats = new DescriptiveStatistics();
		for (double value : data) {
			stats.addValue(value);
		}
	
	}

	@Test
	public void testSlivermanRule() {
		SlivermanRule  rule = new SlivermanRule();
		
		double bandwidth = rule.bandWidth(stats.getVariance(), 2, stats);
		assertEquals(1.09584868977, bandwidth, 0.001);
		bandwidth = rule.bandWidth(stats.getVariance(), 2, data.length);
		assertEquals(1.09584868977, bandwidth, 0.001);
	}
	
	@Test
	public void testScottsRule() {
		ScottsRule  rule = new ScottsRule();
		
		double bandwidth = rule.bandWidth(stats.getVariance(), 2, stats);
		assertEquals(0.00882357722, bandwidth, 0.001);
		bandwidth = rule.bandWidth(stats.getVariance(), 2, data.length);
		assertEquals(0.00882357722, bandwidth, 0.001);
	}
	
	@Test
	public void testMesureOfSpread() {
		MesureOfSpread  rule = new MesureOfSpread();
		
		double bandwidth = rule.bandWidth(stats.getVariance(), 2, stats);
		assertEquals(0.00697035451, bandwidth, 0.001);
		try {
			bandwidth = rule.bandWidth(stats.getVariance(), 2, data.length);
			fail("shuold not be here");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		rule = new MesureOfSpread(60.0, 40.0);
		rule.setQuartileHigh(45.0);
		rule.setQuartileLow(5.0);
	}

}
