package me.math.grid.tiled;

import java.net.UnknownHostException;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import me.math.Vertex;
import me.utils.TransiteEnums;

public class TestDbTiledSpatialGrid {
	
	private Vertex ul = new Vertex(38.941, -77.286);
	private Vertex lr = new Vertex(38.827, -77.078);
	private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);

	@Test
	public void test() {
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
				        overrideFactory("lowerRightCorner", new PointFactory()).
						overrideFactory("upperLeftCorner", new PointFactory()).build();
		
		tester.testBean(DbTiledSpatialGrid.class, configuration);
	}
	
	@Ignore
	@Test
	public void testConstructor() throws UnknownHostException, SQLException {
		
		DbTiledSpatialGrid dbg = new DbTiledSpatialGrid();
		dbg.setUpperLeftCorner(ul.toPoint());
		dbg.setLowerRightCorner(lr.toPoint());
		dbg.setGridSpacingMeters(distance);
		
		dbg.createGrid(dbg.getUpperLeft(), dbg.getLowerRight());
		
		try {
			dbg = new DbTiledSpatialGrid(ul, lr, distance);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
	
	class PointFactory implements Factory<Point> {
		
		private GeometryFactory factory = new GeometryFactory();
		
        @Override
        public Point create() {
            return factory.createPoint(new Coordinate(2,2));
        }
    }

}
