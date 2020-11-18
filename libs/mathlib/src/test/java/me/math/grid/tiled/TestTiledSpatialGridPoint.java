package me.math.grid.tiled;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.math.Vertex;
import me.math.grid.SpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.MinBoundingRectangle;

public class TestTiledSpatialGridPoint extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    @Mock(type=MockType.NICE)
    public SpatialTile sptile;
    
    @Mock
    private SpatialGridPoint point;

	@Test
	@Ignore
	public void testBean() {
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
						overrideFactory("grid", new SpatialTileFactory()).
						overrideFactory("corner", new VertexFactory()).
						ignoreProperty("left").
						ignoreProperty("right").
						ignoreProperty("MBR").
						ignoreProperty("parent").
						ignoreProperty("data").build();
		
		tester.testBean(SpatialGridPoint.class, configuration);
	}
	
	@Test
	public void testConstructor() {
		Vertex v = new Vertex(-77.286, 38.941);
		SpatialGridPoint obj = new SpatialGridPoint(10, 10, v, 20, sptile);
		
		assertNotNull(obj);
	    obj.setCorner(v);

	}
	
	@Test
	public void testJson() {
		ObjectMapper json = new ObjectMapper();
		Vertex v = new Vertex(-77.286, 38.941);
		SpatialGridPoint obj = new SpatialGridPoint(10, 10, v, 20, sptile);
		
		assertNotNull(obj);
	    obj.setCorner(v);

	}
	
	
	@Test
	public void testNodeGridMethods() {
		NodeMock mock = new NodeMock();
		Vertex v = new Vertex(-77.286, 38.941);
		SpatialGridPoint obj = new SpatialGridPoint(10, 10, v, 20, sptile);
		
		expect(sptile.getEntry(EasyMock.anyInt())).andReturn(point).anyTimes();
		expect(point.getIndex()).andReturn(10).anyTimes();
		replayAll();

		assertNull(obj.getLeft());
        obj.setLeft(point);	
        assertNotNull(obj.getLeft());
        obj.setLeft(mock);
        assertNull(obj.getRight());
        obj.setRight(point);
        assertNotNull(obj.getRight());
        obj.setRight(mock);
        obj.setParent(point);
        assertNotNull(obj.getParent());
        obj.setParent(mock);
                
        verifyAll();
        resetAll();
	}
	
	@Test
	public void testToDocument() {
		
		Vertex v = new Vertex(-77.286, 38.941);
		SpatialGridPoint obj = new SpatialGridPoint(10, 10, v, 20, sptile);
		
		expect(sptile.getEntry(EasyMock.anyInt())).andReturn(point).anyTimes();
		expect(point.getIndex()).andReturn(10).anyTimes();
		replayAll();
		
		obj.setDirection(INode.Direction.XLAT);
        obj.setLeft(point);	
        obj.setRight(point);
        obj.setParent(point);
        
        verifyAll();
        resetAll();
        
        
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	class VertexFactory implements Factory<Vertex> {
				
        @Override
        public Vertex create() {
        		return new Vertex(-77.286, 38.941);
        }
    }
	
	class SpatialTileFactory implements Factory<SpatialTile> {
		
        @Override
        public SpatialTile create() {
        		return new SpatialTile();
        }
    }
	
	class INodeFactory implements Factory<INode> {
		
        @Override
        public INode create() {
        		return new NodeMock();
        }
    }
	
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

    private class NodeMock implements INode {

		@Override
		public int getDepth() {
			return 0;
		}

		@Override
		public Vertex getPointVertex() {
			return null;
		}

		@Override
		public Direction getDirection() {
			return null;
		}

		@Override
		public MinBoundingRectangle getMBR() {
			return null;
		}

		@Override
		public boolean contains(Vertex pt) {
			return false;
		}

		@Override
		public INode getLeft() {
			return null;
		}

		@Override
		public void setLeft(INode left_) {			
		}

		@Override
		public INode getRight() {
			return null;
		}

		@Override
		public void setRight(INode right_) {			
		}

		@Override
		public INode getParent() {
			return null;
		}

		@Override
		public SpatialGridPoint getPoint() {
			return null;
		}
    	
    }
	
}
