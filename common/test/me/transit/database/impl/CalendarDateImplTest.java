package me.transit.database.impl;

import static org.junit.Assert.*;

import java.util.Calendar;

import me.transit.database.Agency;
import me.transit.database.CalendarDate;

import org.junit.Test;

public class CalendarDateImplTest {

	@Test
	public void testConstructor() {
		CalendarDate cal = new CalendarDateImpl();
		
		assertNull(cal.getAgency());
		assertEquals(-1, cal.getUUID());
		assertNull(cal.getId());
		assertEquals("0.5", cal.getVersion());
		assertNull(cal.getDate());
		assertEquals(CalendarDate.ExceptionType.UNKNOWN, cal.getExceptionType());
		
	}

	@Test
	public void testGetsAndSets() {
		Agency agency = new AgencyImpl("test");
		CalendarDate cal = new CalendarDateImpl();
		
		assertNull(cal.getAgency());
		assertEquals(-1, cal.getUUID());
		assertNull( cal.getId());
		assertEquals("0.5", cal.getVersion());
		assertNull(cal.getDate());
		assertEquals(CalendarDate.ExceptionType.UNKNOWN, cal.getExceptionType());
		
		Calendar now = Calendar.getInstance();
		cal.setAgency(agency);
		cal.setUUID(10);
		cal.setId("id");
	    cal.setVersion("1.0");
	    cal.setDate( now);
	    cal.setExceptionType(CalendarDate.ExceptionType.ADD_SERVICE);
	    
	    assertEquals(agency, cal.getAgency());
	    assertEquals(10, cal.getUUID());
	    assertEquals("id", cal.getId());
	    assertEquals("1.0", cal.getVersion());
	    assertEquals(now, cal.getDate());
	    assertEquals(CalendarDate.ExceptionType.ADD_SERVICE, cal.getExceptionType());
	}

}
