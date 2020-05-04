package transit.database;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import me.transit.database.Agency;


public class AgencyTest extends  AbstractDatabaseTest {

	private BeanTester tester = new BeanTester();

	@Test
	public void testAgency() {

		Configuration configuration = new ConfigurationBuilder().overrideFactory("MBR", new MBeanFactory.PoloygonFactory()).build();

		tester.testBean(Agency.class, configuration);
	}

	@Test
	public void testJson() throws JsonProcessingException {

		Agency object = this.createAgency();

		String json = mapper.writeValueAsString(object);
		Agency rtn = mapper.readValue(json, Agency.class);

		assertEquals(object, rtn);
	}



}
