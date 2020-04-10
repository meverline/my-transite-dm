package transit.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.TransitStop;

import static org.junit.Assert.assertEquals;

public class TransitStopTest extends  AbstractDatabaseTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testTransitStop() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("MBR", new MBeanFactory.PoloygonFactory())
																.overrideFactory("agency", new MBeanFactory.AgencyFactory())
																.overrideFactory("location", new MBeanFactory.PointFactory()).build();

		tester.testBean(TransitStop.class, configuration);
	}

	@Test
	public void testJson() throws JsonProcessingException {

		TransitStop object = new TransitStop();

		object.setAgency(this.createAgency());
		object.setCode("code");
		object.setDesc("desc");
		object.setId("id");
		object.setLocation(this.lr.toPoint());
		object.setLocationType(TransitStop.LocationType.BOARDING_AREA);
		object.setName("name");
		object.setParentStation(10);
		object.setStopTimezone("EST");
		object.setUrl("http://this.url");
		object.setUUID(400L);
		object.setWheelchairBoarding(TransitStop.WheelChariBoardingType.SOME);
		object.setZoneId("zone");

		String json = mapper.writeValueAsString(object);
		TransitStop stop = mapper.readValue(json, TransitStop.class);

		assertEquals(object, stop);

	}
}
