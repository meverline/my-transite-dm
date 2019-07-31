package transit.database;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.Agency;

public class AgencyTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testAgency() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("MBR", new MBeanFactory.PoloygonFactory()).build();

		tester.testBean(Agency.class, configuration);
	}


}
