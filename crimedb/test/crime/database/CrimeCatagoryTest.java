package crime.database;

import static org.junit.Assert.*;
import me.crime.database.CrimeCatagory;

import org.junit.Test;

public class CrimeCatagoryTest {

	/**
	 * 
	 */
	@Test
	public void test() {
		CrimeCatagory cat = new CrimeCatagory();
		
		assertNull(cat.getCrime());
		assertEquals(0, cat.getId());
	}
	
	
	@Test
	public void testGetAndSet() {
		CrimeCatagory cat = new CrimeCatagory();
		
		cat.setCrime("Larceny");
		
		assertEquals("Larceny", cat.getCrime());
		assertEquals(0, cat.getId());
		
		String xml = "  <crime id='" + "0" + "' >" + "Larceny" + "</crime>";
		
		assertEquals( xml, cat.xml(""));
		
	}

}
