package me.math.kdtree;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import me.math.Vertex;
import me.math.grid.SpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.search.RangeSearch;
import me.utils.TransiteEnums;

public class TestKDTree {

	private Vertex ul = new Vertex(38.941, -77.286);
	private Vertex lr = new Vertex(38.827, -77.078);
	private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
	UniformSpatialGrid grid;
	
	@Before
	public void setUp()
	{
		grid = new UniformSpatialGrid(ul, lr, distance);
	}
	
	@Test
	public void test() throws IOException {
		
		KDTree obj = new KDTree(grid.getGridPoints(), new MockINodeCreator());
		assertNotNull(obj.getRootNode());
		
		KDTree tree = new KDTree(obj.getRootNode());
		
		File tmp = File.createTempFile("kdtest", "tst");
		tmp.deleteOnExit();
		tree.dump(tmp.toString());
		
		PrintWriter writer = new PrintWriter(tmp);
		tree.dump(writer);
		writer.close();
		
		int cnt = 0;
		List<SpatialGridPoint> rtn = null;
		for ( SpatialGridPoint pt : grid.getGridPoints()) {
			RangeSearch search = new RangeSearch(pt.getPointVertex(), 1000);
			
			tree.find(search);	
			tree.searchStats(search);	
			tree.search(search);	
			cnt++;
			if ( cnt > 100 ) {
				break;
			}
		}
		
		RangeSearch search = new RangeSearch(new Vertex(-38.941, 77.286), 1000);
		rtn = tree.find(search);	
		assertTrue(rtn.isEmpty());
		rtn = tree.search(search);	
		assertTrue(rtn.isEmpty());
		
	}
		
	//////////////////////////////////////////////////////////////////
	
	class MockINodeCreator implements INodeCreator {

		@Override
		public INode create(SpatialGridPoint loc, Direction dir, INode parent, int depth) {
			return new KDNode(loc, dir, parent, depth);
		}
		
	}

}
