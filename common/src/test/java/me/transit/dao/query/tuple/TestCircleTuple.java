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
import me.utils.TransiteEnums;

public class TestCircleTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    private Vertex ul = new Vertex(38.941, -77.286);
    private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(1.1);
    	
	@Test
	public void testConstructor() {
		
		CircleTuple obj = new CircleTuple("field", ul.toPoint(), this.distance);		
		obj = new CircleTuple(String.class, "field", ul.toPoint(), this.distance);
		
		assertTrue(obj.hasMultipleCriterion());
			
	}
	
	@Test
	public void testGetDoucmentQuery() {
		
		BasicDBObject mongo = new BasicDBObject();
		CircleTuple obj = new CircleTuple("field", ul.toPoint(), this.distance);
		obj.getDoucmentQuery(mongo);			
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
		
		CircleTuple obj = new CircleTuple("field", ul.toPoint(), this.distance);
		obj.getCriterion(mongo);
		
		obj = new CircleTuple(String.class, "field", ul.toPoint(), this.distance);
		obj.getCriterion(mongo);
		
		obj.getMultipeRestriction(mongo);
		obj.getCriterion(mongo);
	}

}
