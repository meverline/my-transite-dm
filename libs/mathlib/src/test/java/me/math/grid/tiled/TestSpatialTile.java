package me.math.grid.tiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import me.math.grid.SpatialGridPoint;
import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.math.LocalDownFrame;
import me.math.Vertex;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.data.CrossCovData;
import me.math.grid.data.DensityEstimateDataSample;
import me.math.kdtree.MinBoundingRectangle;
import me.utils.TransiteEnums;

public class TestSpatialTile {

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
						overrideFactory("mbr", new MinBoundingRectangleFactory()).
						ignoreProperty("boundingBox").
						ignoreProperty( "grid" ).
						ignoreProperty( "defaulValue" ).
						ignoreProperty( "sparseGrid" ).
						ignoreProperty( "frame" ).
						build();
		
		tester.testBean(SpatialTile.class, configuration);
	}
	
	@Test
	public void testConstructor() {
		SpatialTile obj = new SpatialTile(100, 200, 5, 15);
		
		assertEquals(100, obj.getRowOffset());
		assertEquals(200, obj.getColOffSet());
		assertEquals(5, obj.getIndex());
		assertEquals(15, obj.getTileIndex());
		assertNotNull( obj.getGridPoints());
		assertNotNull( obj.getBoundingBox());
		
		try {
			obj.getNextGridPoint(obj.getEntry(1, 1));
			fail("whoops");
		} catch( Exception ex) {
			assertTrue(true);
		}
		
		obj.handleEnum("key", "value");

	}
	
	@Test
	public void testJson() {
		Vertex v = new Vertex(-77.286, 38.941);
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		LocalDownFrame ldf = new LocalDownFrame(v);
		
		SpatialTile obj = new SpatialTile(0, 0, 5, 15);
		
		obj.createGrid(25, 25, ldf, distance, new CrossCovData(v));
		
		for (SpatialGridPoint pt :  obj.getGrid()) {
			pt.setData( new DensityEstimateDataSample());
		}
		
		for (int j= 0; j < 25; j++) {
			obj.getEntry(j, j).getData().addValue(5.0);
		}
		
		ObjectMapper json = new ObjectMapper();
		
		try {
			String str = json.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			System.out.println(str);
			SpatialTile dup = json.readValue(str, SpatialTile.class);
			
		} catch ( IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testNodeMethods() throws IOException {
		Vertex v = new Vertex(-77.286, 38.941);
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		LocalDownFrame ldf = new LocalDownFrame(v);
		
		SpatialTile obj = new SpatialTile(100, 200, 5, 15);
		
		obj.createGrid(25, 25, ldf, distance, new CrossCovData(v));
		
		assertNotNull( obj.getTree());
				
		@SuppressWarnings("unused")
		List<SpatialGridPoint> points = obj.getGrid();
		for (SpatialGridPoint pt :  obj.getGrid()) {
			pt.setData( new TestData());
		}
		
		File tmp = File.createTempFile("prefix", "suffix");
		tmp.deleteOnExit();
		PrintStream out = new PrintStream(tmp);
		obj.toCSV(out, false);
		obj.toCSV(out, true);
		obj.setBoundingBox( obj.getMbr().toPolygon());
						
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	private class TestData extends AbstractDataSample {

		@Override
		public double getValue() {
			return 0;
		}

		@Override
		public void addValue(double value) {			
		}

		@Override
		public void copy(AbstractDataSample item) {
		}
		
		@Override
		public String hash() {
			return "X";
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	class MinBoundingRectangleFactory implements Factory<MinBoundingRectangle> {
		
        @Override
        public MinBoundingRectangle create() {
        	    return new MinBoundingRectangle();
        	    
        }
    }

}
