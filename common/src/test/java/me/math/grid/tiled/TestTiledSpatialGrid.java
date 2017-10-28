package me.math.grid.tiled;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.sql.SQLException;

import org.junit.Test;

import me.math.Vertex;
import me.utils.TransiteEnums;

public class TestTiledSpatialGrid {

	@Test
	public void test() throws SQLException, IOException {
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		
		TiledSpatialGrid obj = new TiledSpatialGrid(ul, lr, distance);
		assertEquals(distance, obj.getGridSpacingMeters(), 0.01);
		assertEquals(ul, obj.getUpperLeft());
		assertEquals(lr, obj.getLowerRight());
		
		
		obj = new TiledSpatialGrid(distance);
		assertEquals(distance, obj.getGridSpacingMeters(), 0.01);

		obj = new TiledSpatialGrid(ul.toPoint(), lr.toPoint(), distance);
		assertEquals(distance, obj.getGridSpacingMeters(), 0.01);
		assertNotNull(obj.getTiles());
		assertEquals(12, obj.getTiles().size());
		
		File tmp = File.createTempFile("tiletestdump", "test");
		tmp.deleteOnExit();
		
		PrintWriter writer = new PrintWriter(tmp);
		obj.dump(writer);
		writer.close();
		
		PrintStream stream = new PrintStream(tmp);
		obj.toCSV(stream, false);
		stream.close();
		
		assertNotNull(obj.getEntry(1));
		
		try {
			obj.getTree();
			fail("whoops");
		} catch( Exception ex) {
			assertTrue(true);
		}
		
		try {
			obj.getNextGridPoint(obj.get(1, 1, 1));
			fail("whoops");
		} catch( Exception ex) {
			assertTrue(true);
		}
		
		
	}

}
