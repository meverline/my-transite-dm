import static org.junit.Assert.*;

import java.util.List;

import me.datamining.jobs.DensityEstimateLocalJob;
import me.factory.DaoBeanFactory;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.array.SpatialGridPoint;
import me.math.grid.array.UniformSpatialGrid;
import me.math.kdtree.INode;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.utils.TransiteEnums;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class TestUniformSpatialGrid {

	@Test
	public void testGrid() {
		
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
		
		assertNotNull(grid.getNextGridPoint( grid.get(10,10)) );
		
		INode node = grid.create(grid.get(10,10), INode.Direction.XLAT, grid.get(20,20), 0);
		assertNotNull(node);
		assertEquals(node, grid.get(10,10));
		
		KDTree tree = new KDTree(grid.getGridPoints(), grid);
		
		for ( AbstractSpatialGridPoint pt : grid.getGridPoints()) {
			RangeSearch search = new RangeSearch(pt.getPointVertex(), 10);
			tree.search(search);
			assertFalse( search.getResults().isEmpty());
			assertEquals( 1, search.getResults().size());
		}
	
	}
	
	@Test
	public void testDatabaseGrid() {
		
		DaoBeanFactory.initilize();
		TransiteStopDao dao =
				TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));
			
		GeometryFactory factory = new GeometryFactory();
		StopQueryConstraint query = new StopQueryConstraint();
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
			
		Point ur = factory.createPoint( new Coordinate(38.827, -77.286));
		Point ll = factory.createPoint( new Coordinate(38.941, -77.078));
			
	    query.addRectangleConstraint(ur, ll);
	    dao.query(query);
	    
	    DensityEstimateLocalJob job = new DensityEstimateLocalJob(null);
	    job.init(ul, lr, 500);
	    //job.process(stops.iterator(), new TransitStopSpatialSample());
	    
	}

}
