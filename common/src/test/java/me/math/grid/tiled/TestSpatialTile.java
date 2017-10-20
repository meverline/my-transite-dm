package me.math.grid.tiled;

import static org.junit.Assert.*;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import me.math.grid.tiled.TestTileFragament.PoloygonFactory;
import me.math.kdtree.MinBoundingRectangle;

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
