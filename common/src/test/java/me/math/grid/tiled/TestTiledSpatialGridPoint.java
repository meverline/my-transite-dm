package me.math.grid.tiled;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.MinBoundingRectangle;

public class TestTiledSpatialGridPoint extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    @Mock(type=MockType.NICE)
    public SpatialTile sptile;
    
    @Mock
    private TiledSpatialGridPoint point;

	@Test
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
		
		tester.testBean(TiledSpatialGridPoint.class, configuration);
	}
	
	@Test
	public void testConstructor() {
		Vertex v = new Vertex(-77.286, 38.941);
		TiledSpatialGridPoint obj = new TiledSpatialGridPoint(10, 10, v, 20, 20);
		
		assertNotNull(obj);
		obj.setRightNode(10);
	    obj.setLeftNode(20);
	    obj.setParentNode(10);
	    obj.setCorner(v);
	    obj.setGrid(new SpatialTile());
	}
	
	@Test
	public void testNodeGridMethods() {
		NodeMock mock = new NodeMock();
		Vertex v = new Vertex(-77.286, 38.941);
		TiledSpatialGridPoint obj = new TiledSpatialGridPoint(10, 10, v, 20, 20);
		
		expect(sptile.getEntry(EasyMock.anyInt())).andReturn(point).anyTimes();
		expect(point.getIndex()).andReturn(10).anyTimes();
		replayAll();
		
		obj.setGrid(sptile);
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
        
        obj.handleEnum("Direction", INode.Direction.XLAT.toString());
        obj.handleEnum("self", INode.Direction.XLAT.toString());
        
        verifyAll();
        resetAll();
	}
	
	@Test
	public void testToDocument() {
		
		Vertex v = new Vertex(-77.286, 38.941);
		TiledSpatialGridPoint obj = new TiledSpatialGridPoint(10, 10, v, 20, 20);
		
		expect(sptile.getEntry(EasyMock.anyInt())).andReturn(point).anyTimes();
		expect(point.getIndex()).andReturn(10).anyTimes();
		replayAll();
		
		obj.setDirection(INode.Direction.XLAT);
		obj.setGrid(sptile);
        obj.setLeft(point);	
        obj.setRight(point);
        obj.setParent(point);
        
        assertNotNull(obj.toDocument());
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
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Vertex getPointVertex() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Direction getDirection() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MinBoundingRectangle getMBR() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean contains(Vertex pt) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public INode getLeft() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLeft(INode left_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public INode getRight() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setRight(INode right_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public INode getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AbstractSpatialGridPoint getPoint() {
			// TODO Auto-generated method stub
			return null;
		}
    	
    }
	
}
