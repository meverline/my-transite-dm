package me.transite.parser.data;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import me.transit.database.Trip;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.IRoute.RouteType;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.RouteFileHandler;
import me.transit.parser.data.converters.DataConverterFactory;

public class RouteFileHandlerTest extends EasyMockSupport  {

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	private Blackboard blackboard = new Blackboard();
	
	@Mock(MockType.NICE)
	private RouteDao routeDao;
	
	@Mock(MockType.NICE) 
	IGraphDatabaseDAO graphDatabase;
		
	DataConverterFactory factory = DataConverterFactory.create();
	
	private Route route = new Route();

	@Test
	public void test() {
		
		Agency agency = new Agency();
		agency.setName("agencyName");
		agency.setFareUrl("1");
		agency.setVersion("0.0");
		
		route.setShortName("SHORT_NAME");
		route.setId("1");
		route.setAgency(agency);
		route.setLongName("LongName");
		route.setColor("B");
		route.setUUID(100L);
		
		List<Trip> tripList = new ArrayList<>();
		
		route.setTripList(tripList);
		route.setDesc("DESC");
		route.setRouteId("id");
		route.setSortOrder(1);
		route.setTextColor("Black");
		route.setType(RouteType.BUS);
		
		blackboard.setAgency(agency);
		RouteFileHandler testSubject = new RouteFileHandler(blackboard, routeDao, graphDatabase, factory);
		
		try {
			expect(routeDao.loadById(EasyMock.anyString(), EasyMock.anyString())).andReturn(route).anyTimes();
			expect(routeDao.save(EasyMock.anyObject(Route.class))).andReturn(route).anyTimes();
			replayAll();
			
			assertTrue(testSubject.parse("src/test/resources/data/routes.txt"));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
