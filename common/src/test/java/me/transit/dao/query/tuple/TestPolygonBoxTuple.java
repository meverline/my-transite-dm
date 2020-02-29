package me.transit.dao.query.tuple;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import me.math.Vertex;
import me.math.kdtree.MinBoundingRectangle;

public class TestPolygonBoxTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
   
    private List<Point> box = new ArrayList<Point>();
    
    private static final GeometryFactory factory_  = new GeometryFactory();
    
    @Before
    public void setUp() {
    		    
	    MinBoundingRectangle obj = new MinBoundingRectangle();
		obj = new MinBoundingRectangle(new Vertex(38.827, -77.078));
		obj.extend(new Vertex(38.941, -77.286));
	    
		
		Polygon poly = obj.toPolygon();
		
		for ( Coordinate cord : poly.getCoordinates()) {
			box.add( factory_.createPoint(cord));
		}
		
    }
    	
	@Test
	public void testConstructor() {
		PolygonBoxTuple obj = new PolygonBoxTuple("field", box);		
		obj = new PolygonBoxTuple(String.class, "field", box);
		
		assertFalse(obj.hasMultipleCriterion());
			
	}
	
	@Test
	public void testGetDoucmentQuery() {
		
		BasicDBObject mongo = new BasicDBObject();
		PolygonBoxTuple obj = new PolygonBoxTuple("field", box);
		obj.getDoucmentQuery(mongo);			
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
		
		PolygonBoxTuple obj = new PolygonBoxTuple("field", box);
		obj.getCriterion();
		
		obj = new PolygonBoxTuple(String.class, "field", box);
		obj.getCriterion();
		
		obj.getMultipeRestriction(mongo);
		obj.getCriterion();
	}

}
