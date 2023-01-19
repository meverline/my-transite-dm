package me.transit.dao.query.tuple;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class TestTimeTuple  {

	@Test
	public void testConstructor() {

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
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
