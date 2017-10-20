package me.math.grid.tiled;

import static org.junit.Assert.*;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import me.math.grid.tiled.TestTileFragament.PoloygonFactory;

public class TestDbTiledSpatialGrid {

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
				        overrideFactory("lowerRightCorner", new PointFactory()).
						overrideFactory("upperLeftCorner", new PointFactory()).build();
		
		tester.testBean(DbTiledSpatialGrid.class, configuration);
	}
	
	class PointFactory implements Factory<Point> {
		
		private GeometryFactory factory = new GeometryFactory();
		
        @Override
        public Point create() {
            return factory.createPoint(new Coordinate(2,2));
        }
    }

}
