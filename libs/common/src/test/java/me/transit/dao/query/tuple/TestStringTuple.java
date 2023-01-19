package me.transit.dao.query.tuple;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestStringTuple {

	@Test
	public void testConstructor() {
		
		StringTuple obj = new StringTuple("field", "value", StringTuple.MATCH.CONTAINS);

		assertEquals("field", obj.getField());
		assertEquals("value", obj.getValue());
		assertEquals(StringTuple.MATCH.CONTAINS, obj.getMatchType());

		obj = new StringTuple(String.class, "field", "value", StringTuple.MATCH.CONTAINS);

		assertEquals("field", obj.getField());
		assertEquals(String.class, obj.getAlias());
		assertEquals("value", obj.getValue());
		assertEquals(StringTuple.MATCH.CONTAINS, obj.getMatchType());
	
		for ( StringTuple.MATCH logic : StringTuple.MATCH.values()) {
			obj = new StringTuple( "field", "value", logic);
			assertEquals("field", obj.getField());
			assertEquals("value", obj.getValue());
			assertEquals(logic, obj.getMatchType());
		}
		
	}

}
