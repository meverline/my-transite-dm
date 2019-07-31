package transit.database;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.ServiceDate;

public class ServiceDateTest {

	private BeanTester tester = new BeanTester();
	
	@Test
	public void testServiceDate() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("endDate", new MBeanFactory.CalendarFactory())
																.overrideFactory("startDate", new MBeanFactory.CalendarFactory())
																.overrideFactory("agency", new MBeanFactory.AgencyFactory())
																.build();

		tester.testBean(ServiceDate.class, configuration);
	}
}
