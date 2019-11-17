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
import me.transit.dao.TransiteStopDao;
import me.transit.database.Agency;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.TransitStopFileHandler;

public class TransitStopFileHandlerTest extends EasyMockSupport {
	
	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	private Blackboard blackboard = new Blackboard();
	
	@Mock(MockType.NICE)
	private TransiteStopDao transiteStopDao;
	
	private Agency agency = new Agency();
	
	@Mock(MockType.NICE) 
	private IGraphDatabaseDAO graphDatabase;

	@Test
	public void test() {
		agency.setName("TEST AGENCY");
		TransitStopFileHandler testSubject = new TransitStopFileHandler(blackboard, transiteStopDao, graphDatabase);
		
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
