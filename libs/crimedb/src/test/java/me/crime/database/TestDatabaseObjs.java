package me.crime.database;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class TestDatabaseObjs {

	BeanTester tester  = new BeanTester();
	
	private String getBigString() {
		StringBuilder bigString = new StringBuilder(256);
		for ( int ndx = 0; ndx < 256; ndx++) {
			bigString.append("x");
		}
		return bigString.toString();
	}
	
	@Test
	public void testAddress() {
		Configuration configuration = new ConfigurationBuilder().overrideFactory("point", new PointFactory()).build();
		
		tester.testBean(Address.class, configuration);	
		
		Address testSubject = new Address();
		
		testSubject.setAccuracy(10);
		testSubject.setCity("Arlington");
		testSubject.setService("goggle");
		testSubject.setLocation("231 Laceyville Terrace.");
		testSubject.setState("TX");
		testSubject.setZipCode("99001");
		
		GeoPoint pt = new GeoPoint();
		
		pt.setLatX(35.0);
		pt.setLonY(-78.0);
		pt.setSRID(287);
		testSubject.handleObject(pt);
				
		String bigString = this.getBigString();
		testSubject.setLocation(bigString.toString());
		testSubject.handleObject(bigString);
		
		pt.setLonY(78.0);
		testSubject.handleObject(pt);
		
		testSubject.setService("yahoo");
		testSubject.handleObject(pt);
		
	}
		
	@Test
	public void testGeoPoint() {
		tester.testBean(GeoPoint.class);	
	}
	
	@Test
	public void testURCCatagories() {
		tester.testBean(URCCatagories.class);	
		
		URCCatagories obj = new URCCatagories();
		
		assertEquals(URCCatagories.CAT_DEFAULT, obj.getCatagorie());
		assertEquals(URCCatagories.GROUP_DEFAULT, obj.getCrimeGroup());
		assertEquals(0, obj.getId());
		
		String tmp = "URC: id=" + "0" + " Catagorie=" +  URCCatagories.CAT_DEFAULT + " Group=" + URCCatagories.GROUP_DEFAULT;
		
		assertEquals(tmp, obj.asString());
		obj.handleObject("Cat");
	}
	
	@Test
	public void testCrime() {
		
		Configuration configuration = 
				new ConfigurationBuilder().overrideFactory("point", new PointFactory())
										  .overrideFactory("startDate", new CalendarFactory())
										  .overrideFactory("codes", new URCCatagoriesFactory())
										  .overrideFactory("address", new AddressFactory()).build();

		tester.testBean(Crime.class, configuration);	
		
		Crime cr = new Crime("LAR");
		
		cr.setCatagory("Larceny");
		cr.setCounty("Fairfax");
		cr.setDescription("it happend");
		cr.setFile("aFile");
		cr.setCrimeNumber("001-92-XX234");
		cr.setBussiness(this.getBigString());
		cr.setRank(20.0);
		cr.setTime(234.0);
		
		Address addr = new Address();
		cr.setAddress(addr);
		
		URCCatagories cat = new URCCatagories();
		cr.setCodes(cat);
		
		Calendar cal = Calendar.getInstance();
		cr.setStartDate(cal);
		assertNotNull(cr.getDateOnly());
		
		assertNotNull(cr.toTemporalPoint());
		assertNotNull(cr.toDetailedString());
		
		cr.setStartDate(null);
		assertNotNull(cr.getDateOnly());
		assertNotNull(cr.getDate());
		
		assertTrue(cr.isValidState("dc"));
		cr.setCounty("va");
		assertFalse(cr.isValidState("ga"));
		cr.setCounty("dc");
		assertTrue(cr.isValidState("ga"));
		
		assertNotNull(cr.scrub("`~\"\',�  Larceny "));
	
	}
	
	@Test
	public void testCrimeCatagory() {
		tester.testBean(CrimeCatagory.class);
		
		CrimeCatagory cat = new CrimeCatagory("Larceny");
		
		assertEquals("Larceny", cat.getCrime());
		assertEquals(0, cat.getId());
		
		String xml = "  <crime id='" + "0" + "' >" + "Larceny" + "</crime>";
		
		assertEquals( xml, cat.xml(""));
		cat.setCrime("<Larceny>");
		assertNotNull(cat.xml(""));
		cat.setCrime("Lost & Found");
		assertNotNull(cat.xml(""));
		cat.setCrime("Grab\'Go");
		assertNotNull(cat.xml(""));
		cat.setCrime("Grab\"Go");
		assertNotNull(cat.xml(""));
		
	}
	
	@Test
	public void testArrested() {
		
		Configuration configuration = 
				new ConfigurationBuilder().overrideFactory("point", new PointFactory())
										  .overrideFactory("paroleDate", new CalendarFactory()).build();
	
		tester.testBean(Arrested.class, configuration);
		
		Arrested testSubject = new Arrested();
		testSubject.setAddress("1632 Here pl GA");
		testSubject.setAge(40);
		testSubject.setDistance(200);
		testSubject.setId(100L);
		testSubject.setName("Name");
		testSubject.setParoleDate( Calendar.getInstance());
		
		GeoPoint pt = new GeoPoint();
		
		pt.setLatX(35.0);
		pt.setLonY(-78.0);
		pt.setSRID(287);
		testSubject.handleObject(pt);
		
		assertNotNull(testSubject.asText());
		assertNotNull(testSubject.html());
		
		String bigString = this.getBigString();
		testSubject.setAddress(bigString.toString());
		testSubject.handleObject(bigString);

	}
		
	//////////////////////////////////////////////////////////////////////////
	
	class PointFactory implements Factory<Point> {
		
		protected GeometryFactory   factory_  = new GeometryFactory();
		
        @Override
        public Point create() {
        	return factory_.createPoint(new Coordinate(40.0, 40.0 ));
        }
    }

	//////////////////////////////////////////////////////////////////////////

	class AddressFactory implements Factory<Address> {
		
        @Override
        public Address create() {
        	return new Address();
        }
    }
	
	//////////////////////////////////////////////////////////////////////////

	class CalendarFactory implements Factory<Calendar> {
		
        @Override
        public Calendar create() {
        	return Calendar.getInstance();
        }
    }

	//////////////////////////////////////////////////////////////////////////

	class URCCatagoriesFactory implements Factory<URCCatagories> {
		
        @Override
        public URCCatagories create() {
        	return new URCCatagories();
        }
    }


}
