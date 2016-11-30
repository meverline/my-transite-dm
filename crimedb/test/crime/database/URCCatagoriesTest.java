package crime.database;

import static org.junit.Assert.*;
import me.crime.database.URCCatagories;

import org.junit.Test;

public class URCCatagoriesTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {
		URCCatagories obj = new URCCatagories();
		
		assertEquals(URCCatagories.CAT_DEFAULT, obj.getCatagorie());
		assertEquals(URCCatagories.GROUP_DEFAULT, obj.getCrimeGroup());
		assertEquals(0, obj.getId());
		
		String tmp = "URC: id=" + "0" + " Catagorie=" +  URCCatagories.CAT_DEFAULT + " Group=" + URCCatagories.GROUP_DEFAULT;
		
		assertEquals(tmp, obj.asString());
	}
	
	public static URCCatagories createURCCatagories()
	{
		URCCatagories obj = new URCCatagories();
		
		obj.setCatagorie("Catagory");
		obj.setCrimeGroup("E");
		return obj;
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAndSet() {
		URCCatagories obj = new URCCatagories();
		
		obj.setCatagorie("Catagory");
		obj.setCrimeGroup("E");
		
		assertEquals("Catagory", obj.getCatagorie());
		assertEquals("E", obj.getCrimeGroup());
		assertEquals(0, obj.getId());
		
		String tmp = "URC: id=" + "0" + " Catagorie=" +  "Catagory" + " Group=" + "E";
		
		assertEquals(tmp, obj.asString());
	}
	
	

}
