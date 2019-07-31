package transit.database;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.Trip;

public class TripTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testTrip() {
		Configuration configuration = new ConfigurationBuilder()
				.overrideFactory("agency", new MBeanFactory.AgencyFactory())
				.overrideFactory("shape", new RouteGeometryFactory())
				.overrideFactory("service", new ServiceDateFactory()).build();

		tester.testBean(Trip.class, configuration);
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class RouteGeometryFactory implements Factory<RouteGeometry> {

		@Override
		public RouteGeometry create() {
			return new RouteGeometry();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public static class ServiceDateFactory implements Factory<ServiceDate> {

		@Override
		public ServiceDate create() {
			return new ServiceDate();
		}
	}


}
