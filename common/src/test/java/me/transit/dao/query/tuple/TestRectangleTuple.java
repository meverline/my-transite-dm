package me.transit.dao.query.tuple;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.BasicDBObject;

import me.math.Vertex;

public class TestRectangleTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    private Vertex lr = new Vertex(38.827, -77.078);   
    private Vertex ul = new Vertex(38.941, -77.286);
    	
	@Test
	public void testConstructor() {
		
		RectangleTuple obj = new RectangleTuple("field", ul.toPoint(), lr.toPoint());		
		obj = new RectangleTuple(String.class, "field", ul.toPoint(), lr.toPoint());
		
		assertTrue(obj.hasMultipleCriterion());
			
	}
	
	@Test
	public void testGetDoucmentQuery() {
		
		BasicDBObject mongo = new BasicDBObject();
		RectangleTuple obj = new RectangleTuple("field", ul.toPoint(), lr.toPoint());
		obj.getDoucmentQuery(mongo);			
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
		
		RectangleTuple obj = new RectangleTuple("field", ul.toPoint(), lr.toPoint());
		obj.getCriterion(mongo);
		
		obj = new RectangleTuple(String.class, "field", ul.toPoint(), lr.toPoint());
		obj.getCriterion(mongo);
		
		obj.getMultipeRestriction(mongo);
		obj.getCriterion(mongo);
	}

}
