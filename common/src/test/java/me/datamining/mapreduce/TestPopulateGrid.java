package me.datamining.mapreduce;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGridPoint;

public class TestPopulateGrid  extends AbstractMapReduceTest{

	@Test
	public void test() {
		
		SpatialTile tile = this.createTile();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		QueryResults qr = new QueryResults();
		qr.startWrite(ps);
		
		int ndx = 0;
		for (TiledSpatialGridPoint pt :  tile.getGrid()) {
			if ( ndx % 2 == 0) {
				qr.write(ps, pt.getVertex(), pt.getData().getValue());
			}
			ndx++;
		}
		
		qr.endWrite(ps);
		
		PopulateGrid testSubject = new PopulateGrid( TestData.class);
		ByteArrayInputStream dataStream = new ByteArrayInputStream(out.toByteArray());
		
		testSubject.populate(tile, dataStream);
		
		try {
			testSubject.populate(null, dataStream);
			fail("fail");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		try {
			testSubject.populate(tile, null);
			fail("fail");
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testBad() {
		
		try {
			PopulateGrid testSubject = new PopulateGrid(null);
			fail("fail");
		} catch (Exception e) {
			assertTrue(true);
		}
		

	}
		

}
