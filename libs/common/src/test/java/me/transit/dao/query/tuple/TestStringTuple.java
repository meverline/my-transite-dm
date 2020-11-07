package me.transit.dao.query.tuple;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStringTuple {

	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")
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
