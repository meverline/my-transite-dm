package me.datamining.mapreduce;

import java.util.Random;

import me.math.LocalDownFrame;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.data.CrossCovData;
import me.math.grid.tiled.SpatialTile;
import me.utils.TransiteEnums;

public class AbstractMapReduceTest {

	protected SpatialTile createTile() {
		
		Vertex v = new Vertex(38.941, -77.286);
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);		
		LocalDownFrame ldf = new LocalDownFrame(v);
		
		SpatialTile tile = new SpatialTile(100, 200, 5, 15);
		
		tile.createGrid(25, 25, ldf, distance, new CrossCovData(v));
		Random r = new Random();
		
		for (SpatialGridPoint pt :  tile.getGrid()) {
			pt.setData( new TestData(r.nextDouble()*5));
		}
		
		return tile;
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class TestData extends AbstractDataSample {

		double value = 0.0;
		
		public TestData() {
		}
		
		public TestData(double startValue) {
			value = startValue;
		}

		@Override
		public void copy(AbstractDataSample item) {
			item.addValue(value);
		}

		@Override
		public double getValue() {
			return value;
		}

		@Override
		public void addValue(double value) {
			this.value += value;
		}

		@Override
		public String hash() {
			return Long.toString(Double.hashCode(this.getValue()));
		}

	}
}
