package me.math.grid.tiled;

import static org.junit.Assert.*;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.grid.tiled.TestTileFragament.PoloygonFactory;
import me.math.kdtree.INode;
import me.math.kdtree.MinBoundingRectangle;

public class TestTiledSpatialGridPoint {

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
