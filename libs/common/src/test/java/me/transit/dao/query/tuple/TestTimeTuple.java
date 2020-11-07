package me.transit.dao.query.tuple;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class TestTimeTuple  {

	@Test
	public void testConstructor() {

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		@SuppressWarnings("unused")
		TimeTuple obj = new TimeTuple("field", start, end);

		assertEquals("field", obj.getField());
		assertEquals(start, obj.getStartTime());
		assertEquals(end, obj.getEndTime());

		obj = new TimeTuple(String.class, "field", start, end);

		assertEquals("field", obj.getField());
		assertEquals(String.class, obj.getAlias());
		assertEquals(start, obj.getStartTime());
		assertEquals(end, obj.getEndTime());
			
	}
}
