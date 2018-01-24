package transit.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.Test;

import me.transit.database.Agency;
import me.transit.database.CalendarDate;

public class CalendarDateTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		CalendarDate cal = new CalendarDate();
		
		assertNull(cal.getAgency());
		assertEquals(-1, cal.getUUID());
		assertNull(cal.getId());
		assertEquals("0.5", cal.getVersion());
		assertNotNull(cal.getDate());
		assertEquals(CalendarDate.ExceptionType.UNKNOWN, cal.getExceptionType());
		
	}

	/**
	 * 
	 */
	@Test
	public void testGetsAndSets() {
		Agency agency = new Agency("test");
		CalendarDate cal = new CalendarDate();
		
		assertNull(cal.getAgency());
		assertEquals(-1, cal.getUUID());
		assertNull( cal.getId());
		assertEquals("0.5", cal.getVersion());
		assertNotNull(cal.getDate());
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
