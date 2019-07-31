package transit.database;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.CalendarDate;

public class CalendarDateTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testCalendarDate() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("agency", new MBeanFactory.AgencyFactory())
																.overrideFactory("date", new MBeanFactory.CalendarFactory()).build();

		tester.testBean(CalendarDate.class, configuration);
	}
}
