package transit.database;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.transit.database.Agency;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.CalendarDate;

import java.util.Calendar;

public class CalendarDateTest  extends AbstractDatabaseTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testCalendarDate() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("agency", new MBeanFactory.AgencyFactory())
																.overrideFactory("date", new MBeanFactory.CalendarFactory()).build();

		tester.testBean(CalendarDate.class, configuration);
	}

	@Test
	public void testJson() throws JsonProcessingException {

		CalendarDate date = new CalendarDate();
		date.setAgency(this.createAgency());
		date.setDate(Calendar.getInstance());
		date.setExceptionType(CalendarDate.ExceptionType.ADD_SERVICE);
		date.setId("id");
		date.setUUID(200L);
		date.setVersion("0.5");

		String json = mapper.writeValueAsString(date);
		CalendarDate rtn = mapper.readValue(json, CalendarDate.class);

	}
}