package me.math.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import me.datamining.Kernel.Epanechnikov;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.datamining.mapreduce.PopulateGrid;
import me.datamining.mapreduce.QueryResults;
import me.datamining.mapreduce.TiledFinilizeKDE;
import me.datamining.mapreduce.TiledNonAdaptiveKDE;
import me.datamining.metric.TransitStopSpatialSample;
import me.math.Vertex;
import me.math.grid.data.DensityEstimateDataSample;
import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGrid;
import me.math.grid.SpatialGridPoint;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;
import me.utils.TransiteEnums;

public class TestTiledDatabaseGrid extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    @Mock
    private TransiteStopDao dao;

	public static Log log = LogFactory.getLog(TestTiledDatabaseGrid.class);

	/**
	 * 
	 */
	@Ignore
	@Test
	public void test() {

		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);

		double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
		TiledSpatialGrid grid;
		try {
			grid = new TiledSpatialGrid(ul, lr, distance);

			assertEquals(distance, grid.getGridSpacingMeters(), 0.01);
			assertEquals(113, grid.getCols());
			assertEquals(80, grid.getRows());

			assertNotNull(grid.getTiles());
			List<SpatialTile> list = grid.getTiles();
			assertEquals(list.size(), 12);

			int numCells = grid.getTileSize() * grid.getTileSize();
			List<SpatialGridPoint> prev = null;
			for (SpatialTile tile : list) {
				List<SpatialGridPoint> ptList = tile.getGridPoints();
				assertNotNull(ptList);
				assertEquals(numCells, ptList.size());
				if (prev != null) {
					int last = prev.get(ptList.size() - 1).getIndex();
					int first = ptList.get(0).getIndex();
					assertTrue(last + " " + first, last != first);
				}
				prev = ptList;
			}

			int blockSize = grid.getTileSize() * grid.getTileSize();
			for (int row = 0; row < grid.getRows(); row++) {
				for (int col = 0; col < grid.getCols(); col++) {
					SpatialGridPoint pt = grid.getEntry(row, col);
					assertNotNull(pt);
					assertEquals(row, pt.getRow());
					assertEquals(col, pt.getCol());

					int tile = pt.getIndex() / blockSize;
				}
			}

			numCells = grid.getCols() * grid.getRows();
			for (int ndx = 0; ndx < numCells; ndx++) {
				SpatialGridPoint pt = grid.getEntry(ndx);
				assertNotNull(pt);
				assertEquals(pt.getIndex(), ndx);
			}

			for (SpatialTile tile : grid.getTiles()) {
				KDTree tree = tile.getTree();
				for (SpatialGridPoint pt : tile.getGrid()) {
					RangeSearch search = new RangeSearch(pt.getPointVertex(), 10);
					tree.search(search);
					assertFalse(search.getResults().isEmpty());
					assertEquals(1, search.getResults().size());
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	/**
	 * 
	 * @param prefix
	 * @param dir
	 * @param iv
	 * @param tile
	 * @throws Exception
	 */
	protected void dump(String prefix, File dir, boolean iv, SpatialTile tile) throws Exception {
		String xls = prefix + getClass().getSimpleName() + "_" + tile.getIndex() + ".csv";
		File write = new File(dir, xls);

		PrintStream csvStream = new PrintStream(write);
		tile.toCSV(csvStream, iv);
		csvStream.close();
	}
	
	/**
	 * 
	 * @param master
	 * @param objectMapper
	 * @return
	 */
	private SpatialTile cloneTile(SpatialTile master, ObjectMapper objectMapper)
	{
		SpatialTile rtn = null;
		
		try {
			
			File tempFile = File.createTempFile("master", "spatialTile");
			
			objectMapper.writeValue(tempFile, master);
			
			BufferedReader in = new BufferedReader(new FileReader(tempFile));
			StringBuilder builder = new StringBuilder();
			while(in.ready()) {
				builder.append(in.readLine());
			}
			in.close();

			rtn = objectMapper.readValue(builder.toString(), SpatialTile.class);  
						
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rtn;
	}

	/**
	 * 
	 */
	@Ignore
	@Test
	public void testDatabaseGrid() {

		GeometryFactory factory = new GeometryFactory();

		Point ur = factory.createPoint(new Coordinate(38.941, -77.286));
		Point lr = factory.createPoint(new Coordinate(38.827, -77.078));

		StopQueryConstraint query = new StopQueryConstraint();
		query.addRectangleConstraint(lr, ur);
		List<TransitStop> stops = dao.query(query);

		log.info("Number of Stops: " + stops.size());

		TransitStopSpatialSample metric = new TransitStopSpatialSample();
		Vertex point = new Vertex();

		String fileName = getClass().getSimpleName() + ".xml";
		File dir = new File("/tmp");
		File write = new File(dir, fileName);

		long start = System.currentTimeMillis();

		QueryResults output = new QueryResults();

		try {

			log.info(write.toString());
			PrintStream stream = new PrintStream(write);

			output.startWrite(stream);
			for (TransitStop local : stops) {
				point.setLatitudeDegress(local.getLocation().getX());
				point.setLongitudeDegress(local.getLocation().getY());

				output.write(stream, point, metric.getMetric(local));
			}
			output.endWrite(stream);
			stream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {

			double distance = TransiteEnums.DistanceUnitType.MI.toMeters(0.1);
			TiledSpatialGrid grid = new TiledSpatialGrid(ur, lr, distance);
			PopulateGrid pg = new PopulateGrid(DensityEstimateDataSample.class);

			File read = new File(dir, fileName);
			log.info(read.toString());
			log.info("number of tiles: " + grid.getTiles().size());

			for (SpatialTile tile : grid.getTiles()) {
				FileInputStream input = new FileInputStream(read);
				pg.populate(tile, input);
				input.close();
			}

			log.info("N: " + output.getN());
			log.info("Variance: " + output.getVariance());
			log.info("crossCovar: " + grid.getCrossCovariance());

			List<List<SpatialTile>> resultsList = new ArrayList<List<SpatialTile>>();

			TiledNonAdaptiveKDE kde = new TiledNonAdaptiveKDE(grid.getCrossCovariance(), output.getVariance());
			kde.setDenstiyKernel(new Epanechnikov());
			IBandwidth band = new SlivermanRule();
			kde.setXBandWidth(band);
			kde.setYBandWidth(band);
			kde.setCrossCovariance(grid.getCrossCovariance());
			kde.setVariance(output.getVariance());
			kde.setN(output.getN());

			int ndx = 0;
			
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			
			for (SpatialTile master : grid.getTiles()) {

				FileInputStream input = new FileInputStream(read);

				List<SpatialTile> subSum = new ArrayList<SpatialTile>();
				SpatialTile clone = this.cloneTile(master, objectMapper);
				kde.kernalDensityEstimate(clone, input);
				input.close();
				subSum.add(clone);
				ndx++;
				resultsList.add(subSum);
			}

			TiledFinilizeKDE subKde = new TiledFinilizeKDE();

			subKde.setNumSamples(stops.size());
			ndx = 0;
			for (SpatialTile master : grid.getTiles()) {
				subKde.finishKDETile(master, resultsList.get(ndx++));
			}

			String xls = "KDE" + getClass().getSimpleName() + ".csv";
			write = new File(dir, xls);

			PrintStream csvStream = new PrintStream(write);
			grid.toCSV(csvStream, true);
			csvStream.close();

			log.info("done " + (System.currentTimeMillis() - start) / 1000);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

}
