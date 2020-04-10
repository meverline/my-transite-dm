package transit.database;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.math.Vertex;
import me.math.kdtree.MinBoundingRectangle;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.transit.database.Agency;

import static org.junit.Assert.assertEquals;


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
