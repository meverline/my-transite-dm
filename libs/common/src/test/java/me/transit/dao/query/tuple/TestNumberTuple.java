package me.transit.dao.query.tuple;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}
