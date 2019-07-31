package transit.database;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import me.transit.database.RouteStopData;

public class RouteStopDataTest {

	private BeanTester tester = new BeanTester();
	
	@Test
	public void testRouteStopData() {
		tester.testBean(RouteStopData.class);
	}

	
}
