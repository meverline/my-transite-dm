import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.datamining.Kernel.Epanechnikov;
import me.datamining.bandwidth.IBandwidth;
import me.datamining.bandwidth.SlivermanRule;
import me.datamining.mapreduce.PopulateGrid;
import me.datamining.mapreduce.QueryResults;
import me.datamining.mapreduce.TiledFinilizeKDE;
import me.datamining.mapreduce.TiledNonAdaptiveKDE;
import me.datamining.metric.TransitStopSpatialSample;
import me.factory.DaoBeanFactory;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class TestTiledDatabaseGrid {

	public static Log log = LogFactory.getLog(TestTiledDatabaseGrid.class);

	@Ignore
	@Test
	public void test() {
		
		DaoBeanFactory.initilize();
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
	private SpatialTile cloneTile(XStream xstream, SpatialTile tile)
	{
	    String str = xstream.toXML(tile);
	    SpatialTile clone = (SpatialTile) xstream.fromXML(str);
		return clone;
	}
	
	protected void dump(String prefix, File dir, boolean iv, SpatialTile tile) throws Exception
	{
		String xls = prefix + getClass().getSimpleName() + "_" + tile.getIndex() + ".csv";
	    File write = new File(dir, xls);
	    
	    PrintStream csvStream = new PrintStream(write);
	    tile.toCSV(csvStream, iv);
	    csvStream.close();
	}
	
	@Test
	public void testDatabaseGrid() {
		
		XStream xstream = new XStream();
		
		DaoBeanFactory.initilize();
		TransiteStopDao dao =
				TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));
			
		GeometryFactory factory = new GeometryFactory();
		
		Point ur = factory.createPoint( new Coordinate(38.941, -77.286));
		Point lr = factory.createPoint( new Coordinate(38.827, -77.078));
			
		StopQueryConstraint query = new StopQueryConstraint();
	    query.addRectangleConstraint(lr, ur);
	    List<TransitStop> stops = dao.query(query);
	    
	    log.info("Number of Stops: " +stops.size());
	    
	    TransitStopSpatialSample metric = new TransitStopSpatialSample();
	    Vertex point = new Vertex();
	   
	    File dir = new File("/tmp");
	    
	    long start = System.currentTimeMillis();
	    
	    List<String> fileNames = new ArrayList<String>();
	    QueryResults output = new QueryResults();
	    
	    try {
	    	
	    	String fileName = getClass().getSimpleName() + "_"  + fileNames.size() + ".xml";
	    	
	    	File write = new File(dir, fileName);
	    	log.info(write.toString());
			PrintStream stream = new PrintStream(write);
			
			int number = 0;
			output.startWrite(stream);
		    for (TransitStop local : stops) {	    	
		    	point.setLatitudeDegress(local.getLocation().getX());
		    	point.setLongitudeDegress(local.getLocation().getY());
		    	output.write(stream, point, metric.getMetric(local));
		    	
		    	if ( number >= output.getMaxNumber() ) {
		    		output.endWrite(stream);
		    		stream.close();
		    		fileNames.add(write.toString());
		    		
		    		fileName = getClass().getSimpleName() + "_"  + fileNames.size() + ".xml";
		    		write = new File(dir, fileName);
		    		stream = new PrintStream(write);
		    		output.startWrite(stream);
		    		number = 0;
		    	}
		    	number++;
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
			
			for ( String fn : fileNames ) {
				File read = new File(fn);
				log.info(read.toString());
				log.info("number of tiles: " + grid.getTiles().size());
	
				for (SpatialTile tile : grid.getTiles() ) {
					FileInputStream input = new FileInputStream(read);
					pg.populate(tile, input);
					input.close();
				}
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
			for ( SpatialTile master : grid.getTiles()) {	
				for ( String fnRead : fileNames ) {
			    	FileInputStream input = new FileInputStream(fnRead);
			    	
			    	List<SpatialTile> subSum = new ArrayList<SpatialTile>();
			    	SpatialTile clone = this.cloneTile(xstream, master);
					kde.kernalDensityEstimate(clone, input);
					input.close();
					subSum.add(clone);
					ndx++;
					resultsList.add(subSum);		
				}
			}
			
			TiledFinilizeKDE subKde = new TiledFinilizeKDE();
			
			subKde.setNumSamples(stops.size());
			ndx = 0;
			for ( SpatialTile master : grid.getTiles()) {
				subKde.finishKDETile(master, resultsList.get(ndx++));
			}
			
			String xls = "KDE" + getClass().getSimpleName()  + ".csv";
		    File write = new File(dir, xls);
		    
		    PrintStream csvStream = new PrintStream(write);
		    grid.toCSV(csvStream, true);
		    csvStream.close();
			
		    log.info("done " + (System.currentTimeMillis() - start)/1000);
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	    
	}
		
}
