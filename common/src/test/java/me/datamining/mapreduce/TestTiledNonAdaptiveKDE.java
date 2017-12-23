package me.datamining.mapreduce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import me.math.Vertex;
import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGridPoint;

public class TestTiledNonAdaptiveKDE extends AbstractMapReduceTest {

	@Test
	public void test() {
		TiledNonAdaptiveKDE testSubject = new TiledNonAdaptiveKDE(10, 2);
		
		assertEquals(10, testSubject.getCrossCovariance(), 0.01);
		assertEquals(2, testSubject.getVariance(), 0.01);
		assertNotNull(testSubject.getDenstiyKernel());
		assertNotNull(testSubject.getXBandWidth());
		assertNotNull(testSubject.getYBandWidth());
		assertEquals(0, testSubject.getN());
		
		testSubject.setYBandWidth(testSubject.getYBandWidth());
		testSubject.setXBandWidth(testSubject.getXBandWidth());
		testSubject.setDenstiyKernel(testSubject.getDenstiyKernel());
		
		assertNotNull(testSubject.getDenstiyKernel());
		assertNotNull(testSubject.getXBandWidth());
		assertNotNull(testSubject.getYBandWidth());
		
		Vertex ul = new Vertex(38.941, -77.286);
		
		DataResult dataResult = new DataResult(ul, 200);
		testSubject.handleResult(dataResult);
		testSubject.handleResult(null);
		
		testSubject.setCrossCovariance(20);
		testSubject.setVariance(5);
		assertEquals(20, testSubject.getCrossCovariance(), 0.01);
		assertEquals(5, testSubject.getVariance(), 0.01);
		
		testSubject.setN(5);
		assertEquals(5, testSubject.getN());
		
		
	}

	@Test
	public void testKernalDensityEstimate() {
						
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
		
		TiledNonAdaptiveKDE testSubject = new TiledNonAdaptiveKDE(10, 2);
		
		ByteArrayInputStream dataStream = new ByteArrayInputStream(out.toByteArray());
		testSubject.kernalDensityEstimate(tile, dataStream);
	}
	
}
