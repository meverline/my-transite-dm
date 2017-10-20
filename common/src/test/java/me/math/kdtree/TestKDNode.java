package me.math.kdtree;

import static org.junit.Assert.*;

import org.junit.Test;

import me.math.Vertex;
import me.math.grid.array.UniformSpatialGrid;
import me.utils.TransiteEnums;

public class TestKDNode {

	@Test
	public void test() {
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		UniformSpatialGrid grid = new UniformSpatialGrid(ul, lr, distance);
		
		KDNode node = new KDNode(grid.get(10, 10), INode.Direction.XLAT, null, 0);
		
		assertEquals(0, node.getDepth());
		assertEquals(INode.Direction.XLAT, node.getDirection());
		assertNotNull(node.getMBR());
		assertNotNull(node.getPoint());
		assertNotNull(node.getPointVertex());
		assertNull(node.getParent());
		
		node = new KDNode(grid.get(10, 10), INode.Direction.YLON, node, 0);
		assertNotNull(node.getParent());
		
		node.setLeft(node);
		assertNotNull(node.getLeft());
		node.setRight(node);
		assertNotNull(node.getRight());
		
		assertTrue(node.contains(grid.get(10,10).getVertex()));
		
		assertNotNull(node.toString());
		
	}
	
	@Test
	public void testINode() {
		
		INode.Direction.values();
		assertNotNull( INode.Direction.valueOf("YLON"));
		assertNotNull( INode.Direction.valueOf("XLAT"));
		
	}
	



}
