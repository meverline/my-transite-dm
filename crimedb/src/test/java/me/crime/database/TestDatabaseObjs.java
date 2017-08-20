package me.crime.database;

import java.util.Calendar;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class TestDatabaseObjs {

	BeanTester tester  = new BeanTester();
	
	@Test
	public void testAddress() {
		Configuration configuration = new ConfigurationBuilder().overrideFactory("point", new PointFactory()).build();
		
		tester.testBean(Address.class, configuration);	
	}
		
	@Test
	public void testGeoPoint() {
		tester.testBean(GeoPoint.class);	
	}
	
	@Test
	public void testURCCatagories() {
		tester.testBean(URCCatagories.class);	
	}
	
	@Test
	public void testCrime() {
		
		Configuration configuration = 
				new ConfigurationBuilder().overrideFactory("point", new PointFactory())
										  .overrideFactory("startDate", new CalendarFactory())
										  .overrideFactory("codes", new URCCatagoriesFactory())
										  .overrideFactory("address", new AddressFactory()).build();

		tester.testBean(Crime.class, configuration);	
	}
	
	@Test
	public void testCrimeCatagory() {
		tester.testBean(CrimeCatagory.class);
	}
	
	@Test
	public void testArrested() {
		
		Configuration configuration = 
				new ConfigurationBuilder().overrideFactory("point", new PointFactory())
										  .overrideFactory("paroleDate", new CalendarFactory()).build();

		tester.testBean(Arrested.class, configuration);
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
