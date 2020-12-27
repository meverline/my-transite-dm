package transit.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import me.transit.database.*;
import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import java.util.Calendar;

import static org.junit.Assert.assertTrue;

public class TripTest extends AbstractDatabaseTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testTrip() {
		Configuration configuration = new ConfigurationBuilder()
				.overrideFactory("agency", new MBeanFactory.AgencyFactory())
				.overrideFactory("shape", new RouteGeometryFactory())
				.overrideFactory("service", new ServiceDateFactory()).build();

		tester.testBean(Trip.class, configuration);
	}

	@Test
	public void testJson() throws JsonProcessingException {
		Agency agency = this.createAgency();
		RouteGeometry rg = new RouteGeometry();

		rg.setAgency(agency);
		rg.setId("id");
		rg.setShape(this.mbr.toPolygon());
		rg.setUUID(300L);


		ServiceDate sd = new ServiceDate();

		sd.setAgency(agency);
		sd.setEndDate(Calendar.getInstance());
		sd.setStartDate(Calendar.getInstance());
		sd.setId("id");
		sd.setService(ServiceDate.ServiceDays.WEEKDAY_SERVICE);
		sd.setUUID(500L);
		sd.setServiceDayFlag(10);

		StopTime st = new StopTime();

		st.setStopHeadSign("Head Sign");
		st.setStopName("name");
		st.setStopId("537");
		st.setTripId("523");
		st.setDropOffType(StopTime.PickupType.COORDINATE);
		st.setPickupType(StopTime.PickupType.PHONE);

		for (int n = 0; n < 5; n++) {
			st.addArrivalTime(n);
		}

		Trip object = new Trip();

		object.setRouteTripIndex(10);
		object.setDirectionId(Trip.DirectionType.IN_BOUND);
		object.setDocId(-1);
		object.setHeadSign("headSign");
		object.setShape(rg);
		object.setService(sd);
		object.setId("id");
		object.setDocId(-1);
		object.setShortName("shortName");
		object.setUUID(1000L);

		for (int n = 0; n < 5; n++) {
			object.addStopTime(st);
		}

		String [] ignoreFields = { "agency_name" };
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("agencyFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignoreFields));

		String json = mapper.writer(filters).writeValueAsString(object);

		assertTrue(! json.contains("agency_name"));

	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public class RouteGeometryFactory implements Factory<RouteGeometry> {

		private final RouteGeometry rg = new RouteGeometry();

		public RouteGeometryFactory()
		{
			rg.setAgency(createAgency());
			rg.setId("id");
			rg.setShape(mbr.toPolygon());
			rg.setUUID(300L);
		}

		@Override
		public RouteGeometry create() {
			return rg;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public  class ServiceDateFactory implements Factory<ServiceDate> {

		private  final ServiceDate sd = new ServiceDate();

		public ServiceDateFactory()
		{
			sd.setAgency(createAgency());
			sd.setEndDate(Calendar.getInstance());
			sd.setStartDate(Calendar.getInstance());
			sd.setId("id");
			sd.setService(ServiceDate.ServiceDays.WEEKDAY_SERVICE);
			sd.setUUID(500L);
			sd.setServiceDayFlag(10);
		}

		@Override
		public ServiceDate create() {
			return sd;
		}
	}


}
