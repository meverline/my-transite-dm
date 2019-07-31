package transit.database;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.Route;

public class RouteTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testRoute() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("MBR", new MBeanFactory.PoloygonFactory())
															    .overrideFactory("agency", new MBeanFactory.AgencyFactory())
															    .overrideFactory("location", new MBeanFactory.PointFactory()).build();

		tester.testBean(Route.class, configuration);
	}

}
