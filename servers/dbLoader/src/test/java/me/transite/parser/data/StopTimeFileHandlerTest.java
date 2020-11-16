package me.transite.parser.data;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import me.database.nsstore.IDocumentSession;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.dao.TransiteStopDao;
import me.transit.database.Agency;
import me.transit.database.StopTime;
import me.transit.database.Trip;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.RouteTripPair;
import me.transit.parser.data.StopTimeFileHandler;

public class StopTimeFileHandlerTest extends EasyMockSupport {
	
	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	@Mock(MockType.NICE)
	private Blackboard blackboard;
	
	@Mock(MockType.NICE)
	private RouteDao routeDao;
	
	private Agency agency = new Agency();
	
	@Mock(MockType.NICE)
	private TransiteStopDao transiteStopDao;
	
	@Mock(MockType.NICE)
	private IDocumentSession documentDao;
	
	@Mock(MockType.NICE)
	private IGraphDatabaseDAO graphDatabase; 
	
	@Mock(MockType.NICE)
	private Map<String, RouteTripPair> tripMap;
		
	@Mock(MockType.NICE)
	private Trip trip;
		

	@Test
	public void test() {
		RouteTripPair pair = new RouteTripPair("", trip);
		List<StopTime> times = new ArrayList<>();
				
		agency.setName("TEST AGENCY");
		StopTimeFileHandler testSubject = new StopTimeFileHandler(routeDao, transiteStopDao, documentDao, graphDatabase, blackboard);
		
		try {
			expect(blackboard.getAgency()).andReturn(agency).anyTimes();
			expect(blackboard.getTripMap()).andReturn(tripMap).anyTimes();
			expect(tripMap.get(EasyMock.anyString())).andReturn(pair).anyTimes();
			
			List<RouteTripPair> list = new ArrayList<>();
			list.add(pair);
			expect(tripMap.values()).andReturn(list).anyTimes();
			expect(trip.findStopTimeById(EasyMock.anyString())).andReturn(null).anyTimes();
			expect(trip.getStopTimes()).andReturn(times);
			
			replayAll();
			assertTrue(testSubject.parse("src/test/resources/data/" + testSubject.handlesFile()));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


}
