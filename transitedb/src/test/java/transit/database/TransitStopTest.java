package transit.database;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.TransitStop;

public class TransitStopTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testTransitStop() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("MBR", new MBeanFactory.PoloygonFactory())
																.overrideFactory("agency", new MBeanFactory.AgencyFactory())
																.overrideFactory("location", new MBeanFactory.PointFactory()).build();

		tester.testBean(TransitStop.class, configuration);
	}
}
