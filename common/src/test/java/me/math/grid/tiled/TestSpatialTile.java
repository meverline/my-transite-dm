package me.math.grid.tiled;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import me.math.LocalDownFrame;
import me.math.Vertex;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.tiled.TestTileFragament.PoloygonFactory;
import me.math.kdtree.MinBoundingRectangle;
import me.utils.TransiteEnums;

public class TestSpatialTile {

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
						overrideFactory("mbr", new MinBoundingRectangleFactory()).
						ignoreProperty("boundingBox").build();
		
		tester.testBean(SpatialTile.class, configuration);
	}
	
	@Test
	public void testConstructor() {
		SpatialTile obj = new SpatialTile(100, 200, 5, 15);
		
		assertEquals(100, obj.getRowOffset());
		assertEquals(200, obj.getColOffSet());
		assertEquals(5, obj.getIndex());
		assertEquals(15, obj.getTileIndex());
				
		assertEquals(0.0, obj.getGridSizeInMeters(), 0.001);
		
		assertNotNull( obj.getGridPoints());
		assertNotNull( obj.getBoundingBox());
		obj.setRoot(0);
		
		try {
			obj.getNextGridPoint(obj.getEntry(1, 1));
			fail("whoops");
		} catch( Exception ex) {
			assertTrue(true);
		}
		
		obj.handleEnum("key", "value");
		obj.setRoot(10);
	}
	
	@Test
	public void testNodeMethods() throws IOException {
		Vertex v = new Vertex(-77.286, 38.941);
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		LocalDownFrame ldf = new LocalDownFrame(v);
		
		SpatialTile obj = new SpatialTile(100, 200, 5, 15);
		
		obj.createGrid(25, 25, ldf, distance, new AbstractTiledSpatialGrid.CrossCovData(v));
		
		assertNotNull( obj.getTree());
		
		assertNotNull(obj.toDocument());
		
		List<TiledSpatialGridPoint> points = obj.getGrid();
		for (TiledSpatialGridPoint pt :  obj.getGrid()) {
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
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void addValue(double value) {
			// TODO Auto-generated method stub
			
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
