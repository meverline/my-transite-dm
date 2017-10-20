package me.math.grid.tiled;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import static org.easymock.EasyMock.expect;


public class TestTileFragament extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    @Mock
    private SpatialTile tile;
    
    @Mock
    private DbTiledSpatialGrid grid;
    
    @Mock 
    private Polygon polygon;

	@Test
	public void test() {
		
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
						overrideFactory("boundingBox", new PoloygonFactory()).build();
		
		tester.testBean(TileFragament.class, configuration);
		
		
	}
	
	@Test 
	public void testConstructor() {
		
		expect(tile.getIndex()).andReturn(10);
		expect(tile.getBoundingBox()).andReturn( polygon );
		expect(grid.getHeatMapName()).andReturn("name");
		expect(grid.getUUID()).andReturn(100L);
		replayAll();
		
		TileFragament obj = new TileFragament(tile, grid);
		assertEquals(obj.getHeatMapName(), "name");
		assertEquals(obj.getHeatMapUUID(), 100L);
		assertEquals(obj.getIndex(), 10);
		verifyAll();
		resetAll();
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	class PoloygonFactory implements Factory<Polygon> {
		
		private GeometryFactory factory = new GeometryFactory();
		
        @Override
        public Polygon create() {
        	
        	    Coordinate coords[] = new Coordinate[5];
        		for ( int ndx=0; ndx < 5; ndx++ ) {
        			coords[ndx] = new Coordinate(ndx, ndx);
        		}
        		coords[4] = coords[0];
            return factory.createPolygon(coords);
        }
    }

}
