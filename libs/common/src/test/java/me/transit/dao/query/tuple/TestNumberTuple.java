package me.transit.dao.query.tuple;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.bson.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.BasicDBObject;

public class TestNumberTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    	
	@Test
	public void testConstructor() {
		NumberTuple obj = new NumberTuple("field", 100, 10);
		
		assertEquals(100, obj.getHi().intValue());
		assertEquals(10, obj.getLo().intValue());
		assertEquals("field", obj.getField());
		
		obj = new NumberTuple(String.class, "field", 100, 10);
		
		assertEquals(100, obj.getHi().intValue());
		assertEquals(10, obj.getLo().intValue());
		assertEquals("field", obj.getField());
		assertEquals(String.class, obj.getAlias());
		
		for ( NumberTuple.LOGIC logic : NumberTuple.LOGIC.values()) {
			obj = new NumberTuple( "field", 100, logic);
			
			assertEquals(100, obj.getHi().intValue());
			assertEquals("field", obj.getField());
			assertEquals(logic, obj.getLogic());
		}
		
		obj = new NumberTuple( String.class, "field", 100, NumberTuple.LOGIC.EQ);
		
		assertEquals(String.class, obj.getAlias());
		assertEquals(100, obj.getHi().intValue());
		assertEquals("field", obj.getField());
		assertEquals(NumberTuple.LOGIC.EQ, obj.getLogic());
		
	}
	
	@Test
	public void testGetDoucmentQuery() {

		Document mongo = new Document();
		for ( NumberTuple.LOGIC logic : NumberTuple.LOGIC.values()) {
			NumberTuple obj = new NumberTuple( "field", 100, logic);
			
			obj.getDoucmentQuery(mongo);
		}
		
		try {
			NumberTuple obj = new NumberTuple( "field", 100, 10);
			obj.getDoucmentQuery(mongo);
			fail("not here");
		} catch (Exception ex) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		replayAll();
		for ( NumberTuple.LOGIC logic : NumberTuple.LOGIC.values()) {
			NumberTuple obj = new NumberTuple( String.class, "field", 100, logic);
			
			obj.getCriterion();
		}
	
		NumberTuple obj = new NumberTuple( "field", 100, 10);
		obj.getCriterion();
	    obj = new NumberTuple( String.class, "field", 100, 10);
		obj.getCriterion();
		
	}

}
