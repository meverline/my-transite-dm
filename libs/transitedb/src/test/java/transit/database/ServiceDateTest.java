package transit.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.ServiceDate;

import java.util.Calendar;


public class ServiceDateTest extends AbstractDatabaseTest {

	private BeanTester tester = new BeanTester();
	
	@Test
	public void testServiceDate() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("endDate", new MBeanFactory.CalendarFactory())
																.overrideFactory("startDate", new MBeanFactory.CalendarFactory())
																.overrideFactory("agency", new MBeanFactory.AgencyFactory())
																.build();

		tester.testBean(ServiceDate.class, configuration);
	}

	@Test
	public void testJson() throws JsonProcessingException {
		ServiceDate object = new ServiceDate();

		object.setAgency(this.createAgency());
		object.setEndDate(Calendar.getInstance());
		object.setStartDate(Calendar.getInstance());
		object.setId("id");
		object.setService(ServiceDate.ServiceDays.WEEKDAY_SERVICE);
		object.setUUID(500L);
		object.setDocId("id");
		object.setServiceDayFlag(10);

		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("agencyFilter", SimpleBeanPropertyFilter.serializeAllExcept());
		String json = mapper.writer(filters).writeValueAsString(object);
		@SuppressWarnings("unused")
		ServiceDate sd = mapper.readValue(json, ServiceDate.class);

	}
}
