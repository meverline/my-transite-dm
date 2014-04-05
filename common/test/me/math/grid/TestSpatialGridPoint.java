package me.math.grid;

import static org.junit.Assert.*;
import me.datamining.sample.DefaultSample;
import me.math.Vertex;
import me.math.grid.array.SpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.kdtree.INode;
import me.math.kdtree.MinBoundingRectangle;

import org.junit.Test;

public class TestSpatialGridPoint {

	@Test
	public void testCreate() {
		
		Vertex vt = new Vertex(-77.286, 38.941);
		UniformSpatialGrid grid = new UniformSpatialGrid(10);
		SpatialGridPoint obj = new SpatialGridPoint(2, 10, vt, 20, grid);
		
		assertEquals(obj.getRow(), 2);
		assertEquals(obj.getCol(), 10);
		assertEquals(obj.getIndex(), 20);
		assertNotNull(obj.getVertex());
		assertTrue( vt.equals( obj.getVertex()));
		assertNotNull(obj.Grid());
		assertEquals(obj.Grid(), grid);
		
		assertNotNull( obj.dumpInfo());
		assertEquals(obj.getDirection(), INode.Direction.UNKOWN);
		assertEquals(obj.getDepth(), 0);
		assertNull(obj.getMBR());
		assertNull(obj.getLeft());
		assertNull(obj.getRight());
		assertNull(obj.getParent());
		assertNull(obj.getData());
		
	}

	@Test
	public void testNode() {
		
		Vertex vt = new Vertex(-77.286, 38.941);
		UniformSpatialGrid grid = new UniformSpatialGrid(10);
		SpatialGridPoint obj = new SpatialGridPoint(2, 10, vt, 20, grid);
		
		assertEquals(obj.getRow(), 2);
		assertEquals(obj.getCol(), 10);
		assertEquals(obj.getIndex(), 20);
		assertNotNull(obj.getVertex());
		assertTrue( vt.equals( obj.getVertex()));
		assertNotNull(obj.Grid());
		assertEquals(obj.Grid(), grid);
		
		assertNotNull( obj.dumpInfo());
		
		obj.setLeft(obj);
		obj.setRight(obj);
		obj.setParent(obj);
		
		MinBoundingRectangle mbr = new MinBoundingRectangle();
		
		obj.setMBR(mbr);
		obj.setDirection(INode.Direction.XLAT);
		obj.setDepth(50);
		
		assertEquals(obj.getDirection(), INode.Direction.XLAT);
		assertEquals(obj.getDepth(), 50);
		
		assertNotNull(obj.getMBR());
		assertEquals(obj.getMBR(), mbr);
		assertNotNull(obj.getLeft());
		assertEquals(obj, obj.getLeft());
		assertNotNull(obj.getRight());
		assertEquals(obj, obj.getRight());
		assertNotNull(obj.getParent());
		assertEquals(obj, obj.getParent());
		
		SpatialGridData data = new DefaultSample();
		obj.setData(data);
		assertNotNull(obj.getData());
		assertEquals(obj.getData(), data);
		
	}

}
