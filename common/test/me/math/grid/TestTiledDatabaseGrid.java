package me.math.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import me.math.Vertex;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import me.utils.TransiteEnums;

import org.junit.Test;

public class TestTiledDatabaseGrid {

	@Test
	public void test() {
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		TiledSpatialGrid grid = new TiledSpatialGrid(ul, lr, distance);
		
		assertEquals(distance, grid.getGridSpacingMeters(), 0.01);
		assertEquals(113, grid.getCols());
		assertEquals(80, grid.getRows());
		
		assertNotNull( grid.getTiles());
		List<SpatialTile> list = grid.getTiles();
		assertEquals(list.size(), 12);
		
		int numCells = grid.getTileSize() * grid.getTileSize();
		List<AbstractSpatialGridPoint> prev = null;
		for ( SpatialTile tile : list ) {
			List<AbstractSpatialGridPoint> ptList = tile.getGridPoints();
			assertNotNull(ptList);
			assertEquals(numCells, ptList.size());
			if ( prev != null ) {
				int last = prev.get(ptList.size()-1).getIndex();
				int first = ptList.get(0).getIndex();
				assertTrue(last + " " + first, last != first);
			}
			prev = ptList;
		}
		
		int blockSize = grid.getTileSize() * grid.getTileSize();
		for ( int row = 0; row < grid.getRows(); row++) {
			for ( int col=0; col < grid.getCols(); col++ ) {
				TiledSpatialGridPoint pt = grid.getEntry(row, col);
				assertNotNull(pt);
				assertEquals( row, pt.getRow());
				assertEquals( col, pt.getCol());

				int tile = pt.getIndex()/blockSize;
				assertEquals(pt.getTileIndex(), tile);
			}
		}
		
		numCells = grid.getCols() * grid.getRows();
		for ( int ndx = 0; ndx < numCells; ndx++ ) {
			TiledSpatialGridPoint pt = grid.getEntry(ndx);
			assertNotNull(pt);
			assertEquals(pt.getIndex(), ndx);
		}
		
		for ( SpatialTile tile : grid.getTiles()) {
			KDTree tree = tile.getTree();
			for ( TiledSpatialGridPoint pt : tile.getGrid()) {
				RangeSearch search = new RangeSearch(pt.getPointVertex(), 10);
				tree.search(search);
				assertFalse( search.getResults().isEmpty());
				assertEquals( 1, search.getResults().size());
			}		
		}
		
	}

}
