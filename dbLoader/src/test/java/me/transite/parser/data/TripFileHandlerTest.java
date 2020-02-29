package me.transite.parser.data;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import me.database.mongo.DocumentDao;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.TripFileHandler;

public class TripFileHandlerTest extends EasyMockSupport {
	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	private Blackboard blackboard = new Blackboard();
	
	@Mock(MockType.NICE)
	private RouteDao routeDao;
	
	@Mock(MockType.NICE)
	private IGraphDatabaseDAO graphDatabase;
	
	@Mock(MockType.NICE)
	private DocumentDao documentDao;
	
	private Agency agency = new Agency();
	
	@Mock(MockType.NICE)
	private Route route;

	@Test
	public void test() {
		agency.setName("TEST AGENCY");
		TripFileHandler testSubject = new TripFileHandler(blackboard, routeDao, graphDatabase, documentDao);
				
		try {
			blackboard.setAgency(agency);
			expect(routeDao.loadById(EasyMock.anyString(), EasyMock.anyString())).andReturn(route).anyTimes();
			expect(route.getShortName()).andReturn("SHORT_NAME").anyTimes();
			replayAll();
			assertTrue(testSubject.parse("src/test/resources/data/" + testSubject.handlesFile()));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
