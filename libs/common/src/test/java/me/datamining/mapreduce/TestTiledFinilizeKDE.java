package me.datamining.mapreduce;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import me.math.grid.tiled.SpatialTile;

public class TestTiledFinilizeKDE extends AbstractMapReduceTest {
	
	@Test
	public void test() {
		
		SpatialTile zeroTile = this.createTile();
		
		List<SpatialTile> list = new ArrayList<>();
		for ( int ndx = 0; ndx < 10; ndx++ ) {
			list.add(this.createTile());
		}
		
		TiledFinilizeKDE testSubject = new TiledFinilizeKDE();
		
		assertEquals(0, testSubject.getNumSamples());
		testSubject.setNumSamples(10);
		assertEquals(10, testSubject.getNumSamples());
		
		testSubject.finishKDETile(zeroTile, list);

	}
	
}
