package me.transite.parser.data;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import me.transit.dao.RouteGeometryDao;
import me.transit.database.Agency;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.ShapeFileHandler;

public class ShapeFileHandlerTest extends EasyMockSupport {
	
	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	private Blackboard blackboard = new Blackboard();
	
	@Mock(MockType.NICE)
	private RouteGeometryDao routeGeometryDao;
	
	private Agency agency = new Agency();;

	@Test
	public void test() {
		agency.setName("TEST AGENCY");
		ShapeFileHandler testSubject = new ShapeFileHandler(routeGeometryDao, blackboard);
		
		try {
			blackboard.setAgency(agency);
			replayAll();
			assertTrue(testSubject.parse("src/test/resources/data/" + testSubject.handlesFile()));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
