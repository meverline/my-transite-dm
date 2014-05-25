package me.math.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.datamining.jobs.DensityEstimateLocalJob;
import me.datamining.mapreduce.DataResult;
import me.datamining.mapreduce.PopulateGrid;
import me.datamining.mapreduce.QueryResults;
import me.datamining.mapreduce.TiledFinilizeKDE;
import me.datamining.mapreduce.TiledNonAdaptiveKDE;
import me.datamining.metric.TransitStopSpatialSample;
import me.factory.DaoBeanFactory;
import me.math.Vertex;
import me.math.grid.array.UniformSpatialGrid;
import me.math.grid.data.DensityEstimateDataSample;
import me.math.grid.tiled.SpatialTile;
import me.math.grid.tiled.TiledSpatialGrid;
import me.math.grid.tiled.TiledSpatialGridPoint;
import me.math.kdtree.KDTree;
import me.math.kdtree.search.RangeSearch;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;
import me.utils.TransiteEnums;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class TestTiledDatabaseGrid {

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
				AbstractSpatialGridPoint pt = grid.getEntry(ndx);
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
	 * @param tile
	 * @return
	 */
	private SpatialTile cloneTile(SpatialTile tile)
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		SpatialTile clone = null;
		try {
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			
			out.writeObject(tile);
			
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream input = new ObjectInputStream(byteIn);
			
			clone = (SpatialTile) input.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	@Test
	public void testDatabaseGrid() {
		
		TransiteStopDao dao =
				TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));
			
		GeometryFactory factory = new GeometryFactory();
		StopQueryConstraint query = new StopQueryConstraint();
		
		//Vertex ul = new Vertex(38.941, -77.286);
		//Vertex lr = new Vertex(38.827, -77.078);
			
		Point ur = factory.createPoint( new Coordinate(38.827, -77.286));
		Point ll = factory.createPoint( new Coordinate(38.941, -77.078));
			
	    query.addRectangleConstraint(ur, ll);
	    List<TransitStop> stops = dao.query(query);
	    
	    TransitStopSpatialSample metric = new TransitStopSpatialSample();
	    Vertex point = new Vertex();
	    
	    String fileName = getClass().getSimpleName() + ".xml";
	    File dir = new File(System.getProperty("java.io.tmpdir"));
	    File write = new File(dir, fileName);
	    try {
			PrintStream stream = new PrintStream(write);
			
			QueryResults output = new QueryResults();
		    
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
		}
	    
		try {
			TiledSpatialGrid grid = new TiledSpatialGrid(ur, ll, 500);
			
			PopulateGrid pg = new PopulateGrid(DensityEstimateDataSample.class);
			FileInputStream input = new FileInputStream(new File(dir, fileName));
			
			for (SpatialTile tile : grid.getTiles()) {
				pg.populate(tile, input);
			}
			
			List<List<SpatialTile>> resultsList = new ArrayList<List<SpatialTile>>();
			List<SpatialTile> compute = new ArrayList<SpatialTile>();
			
			TiledNonAdaptiveKDE kde = new TiledNonAdaptiveKDE(grid.getCrossCovariance(), pg.getVariance());
			for ( SpatialTile master : grid.getTiles()) {
				
				List<SpatialTile> subSum = new ArrayList<SpatialTile>();
				for ( SpatialTile tile : grid.getTiles() ) {
					compute.add(tile);
					if ( compute.size() == 4 ) {
						SpatialTile clone = this.cloneTile(master);
						kde.kernalDensityEstimate(clone, compute);
						compute.clear();
						subSum.add(clone);
					}
				}
				
				if ( ! compute.isEmpty() ) {
					SpatialTile clone = this.cloneTile(master);
					kde.kernalDensityEstimate(clone, compute);
					compute.clear();
					subSum.add(clone);
				}
				
				resultsList.add(subSum);	
			}
			
			TiledFinilizeKDE subKde = new TiledFinilizeKDE();
			
			subKde.setNumSamples(stops.size());
			int ndx = 0;
			for ( SpatialTile master : grid.getTiles()) {
				subKde.finishKDETile(master, resultsList.get(ndx++));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	}

}
