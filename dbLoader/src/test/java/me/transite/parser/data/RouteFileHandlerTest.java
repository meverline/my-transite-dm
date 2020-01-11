package me.transite.parser.data;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.RouteFileHandler;
import me.transit.parser.data.converters.DataConverterFactory;

public class RouteFileHandlerTest extends EasyMockSupport  {

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	@Mock(MockType.NICE)
	private Blackboard blackboard;
	
	@Mock(MockType.NICE)
	private RouteDao routeDao;
	
	@Mock(MockType.NICE) 
	IGraphDatabaseDAO graphDatabase;
	
	DataConverterFactory factory = DataConverterFactory.create();

	@Test
	public void test() {
		RouteFileHandler testSubject = new RouteFileHandler(blackboard, routeDao, graphDatabase, factory);
		
		try {
			replayAll();
			assertTrue(testSubject.parse("src/test/resources/data/routes.txt"));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
