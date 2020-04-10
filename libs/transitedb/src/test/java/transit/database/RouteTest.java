package transit.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import me.transit.database.*;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import java.util.Calendar;

import static org.junit.Assert.assertNotNull;

public class RouteTest extends AbstractDatabaseTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testRoute() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("MBR", new MBeanFactory.PoloygonFactory())
															    .overrideFactory("agency", new MBeanFactory.AgencyFactory())
															    .overrideFactory("location", new MBeanFactory.PointFactory()).build();

		tester.testBean(Route.class, configuration);
	}

	@Test
	public  void testJson() throws JsonProcessingException {

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
		sd.setDocId("id");
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

		Trip trip = new Trip();

		trip.setRouteTripIndex(10);
		trip.setDirectionId(Trip.DirectionType.IN_BOUND);
		trip.setDocId("docId");
		trip.setHeadSign("headSign");
		trip.setShape(rg);
		trip.setService(sd);
		trip.setId("id");
		trip.setDocId("docId");
		trip.setShortName("shortName");
		trip.setUUID(1000L);

		for (int n = 0; n < 5; n++) {
			trip.addStopTime(st);
		}

		Route object = new Route();
		object.setAgency(agency);;
		object.setColor("blue");
		object.setDesc("desc");
		object.setDocId("id");
		object.setId("id");
		object.setLongName("longName");
		object.setShortName("shortName");
		object.setRouteId("id2");
		object.setSortOrder(2);
		object.setTextColor("White");
		object.setType(IRoute.RouteType.BUS);
		object.setUrl("htttt");
		object.setUUID(10000L);

		for (int n = 0; n < 5; n++) {
			object.getTripList().add(trip);
		}

		String [] ignoreFields = { "agency_name" };
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("agencyFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignoreFields));

		String json = mapper.writer(filters).writeValueAsString(object);

		assertNotNull(json);

	}

}
