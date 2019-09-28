package transit.database;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import me.transit.database.RouteDocument;

public class RouteDocumentTest {


	private BeanTester tester = new BeanTester();

	@Test
	public void testTransitStop() {

		tester.testBean(RouteDocument.class);
	}
}
