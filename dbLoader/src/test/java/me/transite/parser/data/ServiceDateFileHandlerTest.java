package me.transite.parser.data;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import me.transit.dao.ServiceDateDao;
import me.transit.database.Agency;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.ServiceDateFileHandler;

public class ServiceDateFileHandlerTest extends EasyMockSupport  {

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	private Blackboard blackboard = new Blackboard();
	
	@Mock(MockType.NICE)
	private ServiceDateDao serviceDao;
		

	@Test
	public void test() {
		Agency agency = new Agency();
		agency.setName("agencyName");
		agency.setFareUrl("1");
		agency.setVersion("0.0");
		
		blackboard.setAgency(agency);
		ServiceDateFileHandler testSubject = new ServiceDateFileHandler(serviceDao, blackboard);
		
		try {
			replayAll();
			assertTrue(testSubject.parse("src/test/resources/data/" + testSubject.handlesFile()));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
