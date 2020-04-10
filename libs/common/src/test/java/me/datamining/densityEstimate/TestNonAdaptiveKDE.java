package me.datamining.densityEstimate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import me.datamining.SpatialSamplePoint;
import me.datamining.Kernel.Epanechnikov;
import me.datamining.Kernel.Gaussian;
import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.ScottsRule;
import me.datamining.bandwidth.SlivermanRule;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.grid.data.DensityEstimateDataSample;
import me.utils.TransiteEnums;

public class TestNonAdaptiveKDE {

	@Test
	public void testConstructors() {
		NonAdaptiveKDE kde = new NonAdaptiveKDE();
		
		IDensityKernel kernel = new Epanechnikov();
		IBandwidth bw = new SlivermanRule();
		kde = new NonAdaptiveKDE(kernel, bw, bw);
		
		assertEquals(kernel, kde.getDenstiyKernel());
		assertEquals(bw, kde.getXBandWidth());
		assertEquals(bw, kde.getYBandWidth());
		assertNotNull( kde.getXstats());
		assertNotNull( kde.getYstats());
		
		kernel = new Gaussian();
		bw = new ScottsRule();
				
		@SuppressWarnings("unused")
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
	}
	
	@Test
	public void testSetGets() throws IOException
	{
		NonAdaptiveKDE kde = new NonAdaptiveKDE();
		
		IDensityKernel kernel = new Gaussian();
		IBandwidth bw = new ScottsRule();
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		kde.setDenstiyKernel(kernel);
		assertEquals(kernel, kde.getDenstiyKernel());
		kde.setXBandWidth(bw);
		assertEquals(bw, kde.getXBandWidth());
		kde.setYBandWidth(bw);
		assertEquals(bw, kde.getYBandWidth());
		kde.setGrid(grid);
		assertEquals(grid, kde.getGrid());
		List<SpatialSamplePoint> data = new ArrayList<SpatialSamplePoint>();
		kde.setSampleValues(data);
		assertEquals(data, kde.getSampleValues());
		kde.setCrossCovariance(34.5);
		assertEquals(34.5, kde.getCrossCovariance(), 0.01);
		
		kde.init(grid);
		
		File tmp = File.createTempFile("prefix", "suffix");
		tmp.deleteOnExit();
		kde.dumpGrid(tmp.toString());
	
	}
	
	@Test 
	public void testDensityEstimate()
	{
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.2);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		List<SpatialSamplePoint> data = new ArrayList<SpatialSamplePoint>();
		
		for (AbstractSpatialGridPoint pt  : grid.getGridPoints()) {
			double value = Math.random()*10;
			if ( (int) value % 2 == 0) {
				pt.setData( new DensityEstimateDataSample());
				pt.getData().addValue(Math.random()*10);
				data.add(new SpatialSamplePointMock(pt));
			}
		}
		
		NonAdaptiveKDE kde = new NonAdaptiveKDE();
		kde.setSampleValues(data);
		kde.kernalDensityEstimate(grid);
	}
	
	private class SpatialSamplePointMock implements SpatialSamplePoint {
		
		private AbstractSpatialGridPoint pt;
		
		public SpatialSamplePointMock(AbstractSpatialGridPoint pt) {
			this.pt = pt;
		}

		@Override
		public double getSampleValue() {
			return pt.getData().getValue();
		}

		@Override
		public double getValue() {
			return pt.getData().getValue();
		}

		@Override
		public double getMin() {
			return pt.getData().getValue();
		}

		@Override
		public double getMax() {
			return pt.getData().getValue();
		}

		@Override
		public double getNumber() {
			return 1;
		}

		@Override
		public double average() {
			return 0;
		}

		@Override
		public double standardDeviation() {
			return pt.getData().getValue();
		}

		@Override
		public boolean isChecked() {
			return false;
		}

		@Override
		public void setChecked(boolean flag) {			
		}

		@Override
		public void addSampleData(Object data) {
		}

		@Override
		public AbstractSpatialGridPoint getGridPoint() {
			return pt;
		}

		@Override
		public void setGridPoint(AbstractSpatialGridPoint aPoint) {			
		}
		
	}

}
