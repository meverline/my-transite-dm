package crime.database;

import static org.junit.Assert.*;

import java.util.Calendar;

import me.crime.database.Address;
import me.crime.database.Crime;
import me.crime.database.URCCatagories;

import org.junit.Test;

public class CrimeTest {

	@Test
	public void testConstructor() {
		Crime cr = new Crime();
		
		assertNull(cr.getCatagory());
		assertEquals("", cr.getCounty());
		assertNotNull( cr.getCrimeNumber());
		assertEquals("", cr.getDescription());
		assertEquals("", cr.getFile());
		assertEquals("", cr.getBussiness());
		
		assertNull(cr.getAddress());
		assertNull(cr.getStartDate());
		assertNull(cr.getCodes());
		
		assertEquals(0, cr.getId());
		assertEquals(0.0, cr.getRank(), 0.01);
		assertEquals(0.0, cr.getTime(), 0.01);
		
	}
	
	@Test
	public void testGetAndSet() {
		Crime cr = new Crime();
		
		cr.setCatagory("Larceny");
		cr.setCounty("Fairfax");
		cr.setDescription("it happend");
		cr.setFile("aFile");
		cr.setCrimeNumber("001-92-XX234");
		cr.setBussiness("Apartment");
		cr.setRank(20.0);
		cr.setTime(234.0);
		
		Address addr = AddressTest.createAddress();
		cr.setAddress(addr);
		
		URCCatagories cat = URCCatagoriesTest.createURCCatagories();
		cr.setCodes(cat);
		
		Calendar cal = Calendar.getInstance();
		cr.setStartDate(cal);
		
		assertEquals("Larceny", cr.getCatagory());
		assertEquals("Fairfax", cr.getCounty());
		assertEquals("001-92-XX234", cr.getCrimeNumber());
		assertEquals("it happend", cr.getDescription());
		assertEquals("aFile", cr.getFile());
		assertEquals("Apartment", cr.getBussiness());
		assertEquals(20.0, cr.getRank(), 0.01);
		assertEquals(234.0, cr.getTime(), 0.01);
		
		assertEquals(addr, cr.getAddress());
		assertEquals(cal, cr.getStartDate());
		assertEquals(cat, cr.getCodes());
		
		assertNotNull( cr.toDetailedString());
		assertNotNull( cr.toTemporalPoint());
				
	}

}
