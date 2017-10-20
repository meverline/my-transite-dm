package me.math.grid.array;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.KDTree;
import me.utils.TransiteEnums;

public class TestUniformSpatialGrid {

	@Test
	public void test() {
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		assertEquals(distance, grid.getGridSpacingMeters(), 0.01);
		assertEquals(113, grid.getCols());
		assertEquals(80, grid.getRows());
		assertNotNull(grid.get(10, 10));
		
		List<AbstractSpatialGridPoint> list = grid.getGridPoints();
		assertNotNull(list);
		assertEquals(113*80, list.size());
		
		List<SpatialGridPoint> temp = grid.toList();
		assertNotNull(temp);
		assertEquals(113*80, temp.size());
		
		grid = new UniformSpatialGrid(ul.toPoint(), lr.toPoint(), distance);
		
		assertEquals(distance, grid.getGridSpacingMeters(), 0.01);
		assertEquals(113, grid.getCols());
		assertEquals(80, grid.getRows());
		assertNotNull(grid.get(10, 10));
		
		list = grid.getGridPoints();
		assertNotNull(list);
		assertEquals(113*80, list.size());
		
		temp = grid.toList();
		assertNotNull(temp);
		assertEquals(113*80, temp.size());
	}
	
	@Test
	public void testCSV() throws IOException {
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		File fp = File.createTempFile("usgt", "gt");
		
		grid.toCSVFile(fp.toString());
	}
	
	@Test
	public void testGet() throws IOException {
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		assertEquals(113, grid.getCols());
		assertEquals(80, grid.getRows());
		assertNotNull(grid.get(10, 10));
		assertNull(grid.get(85, 200));
		assertNull(grid.get(-1, -1));
	}
	
	@Test
	public void testFindGridPont() throws IOException {
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		Vertex pt = new Vertex(38.750, -77.125);
		
		//assertNotNull( grid.findGridPont(pt.toPoint()));
	}
	
	@Test
	public void testGetNextGridPoint() throws IOException {
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		assertNotNull(grid.getNextGridPoint( grid.get(10,10)) );
		assertNotNull(grid.getNextGridPoint( grid.get(0,10)) );
		assertNotNull(grid.getNextGridPoint( grid.get(10,0)) );
		
		SpatialGridPoint gp = new SpatialGridPoint(85, 10, null, 0, grid);
		
		assertNull(grid.getNextGridPoint( gp ) );
		
		gp = new SpatialGridPoint(10, 140, null, 0, grid);
		
		assertNull(grid.getNextGridPoint( gp ) );
	}
	
	@Test
	public void testCreate() throws IOException {
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		INode node = grid.create(grid.get(10,10), INode.Direction.XLAT, grid.get(20,20), 0);
		assertNotNull(node);
		assertEquals(node, grid.get(10,10));
		
	}
	
	@Test
	public void testTree() throws IOException {
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
				
		@SuppressWarnings("unused")
		KDTree tree = grid.getTree();
	}

}
