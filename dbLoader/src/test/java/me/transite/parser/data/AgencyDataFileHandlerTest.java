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

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.AgencyDao;
import me.transit.parser.data.AgencyDataFileHandler;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.converters.DataConverterFactory;

public class AgencyDataFileHandlerTest extends EasyMockSupport  {

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	@Mock(MockType.NICE)
	private Blackboard blackboard;
	
	@Mock(MockType.NICE)
	private AgencyDao agencyDao;
	
	@Mock(MockType.NICE) 
	IGraphDatabaseDAO graphDatabase;
	
	DataConverterFactory factory = DataConverterFactory.create();
	
	@Test
	public void test() {
		AgencyDataFileHandler testSubject = new AgencyDataFileHandler(blackboard, agencyDao, graphDatabase, factory);
		
		try {
			expect(agencyDao.findByName(EasyMock.anyString())).andReturn(null);
			replayAll();
			assertTrue(testSubject.parse("src/test/resources/data/agency.txt"));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
