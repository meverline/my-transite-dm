package me.math.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import me.datamining.densityEstimate.NonAdaptiveKDE;
import me.datamining.jobs.DensityEstimateLocalJob;
import me.datamining.metric.IDataProvider;
import me.datamining.metric.TransitStopSpatialSample;
import me.math.Vertex;
import me.math.grid.array.UniformSpatialGrid;
import me.math.kdtree.INode;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;
import me.utils.TransiteEnums;

public class TestUniformSpatialGrid extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    @Mock
    private TransiteStopDao dao;

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

		List<SpatialGridPoint> list = grid.getGridPoints();
		assertNotNull(list);
		assertEquals(113 * 80, list.size());

		List<SpatialGridPoint> temp = grid.toList();
		assertNotNull(temp);
		assertEquals(113 * 80, temp.size());

		assertNotNull(grid.getNextGridPoint(grid.get(10, 10)));

		INode node = grid.create(grid.get(10, 10), INode.Direction.XLAT, grid.get(20, 20), 0);
		assertNotNull(node);
		assertEquals(node, grid.get(10, 10));

		KDTree tree = new KDTree(grid.getGridPoints(), grid);

		for (SpatialGridPoint pt : grid.getGridPoints()) {
			RangeSearch search = new RangeSearch(pt.getPointVertex(), 10);
			tree.search(search);
			assertFalse(search.getResults().isEmpty());
			assertEquals(1, search.getResults().size());
		}

	}

	@Ignore
	@Test
	public void testDatabaseGrid() {

		GeometryFactory factory = new GeometryFactory();
		StopQueryConstraint query = new StopQueryConstraint();

		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);

		Point ur = factory.createPoint(new Coordinate(38.827, -77.286));
		Point ll = factory.createPoint(new Coordinate(38.941, -77.078));

		query.addRectangleConstraint(ur, ll);
		List<TransitStop> stops = dao.query(query);
		List<IDataProvider> data = new ArrayList<IDataProvider>();

		data.addAll(stops);

		DensityEstimateLocalJob job = new DensityEstimateLocalJob(null, new NonAdaptiveKDE());
		job.init(ul, lr, 500);
		job.process(data.iterator(), new TransitStopSpatialSample());

	}

}
