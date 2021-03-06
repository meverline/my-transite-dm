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
import me.transit.dao.CalendarDateDao;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.CalendarDateFileHandler;
import me.transit.parser.data.converters.DataConverterFactory;

public class CalendarDateFileHandlerTest extends EasyMockSupport  {

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);
	
	private Blackboard blackboard = new Blackboard();
	
	@Mock(MockType.NICE)
	private CalendarDateDao calendarDateDao;
	
	@Mock(MockType.NICE) 
	IGraphDatabaseDAO graphDatabase;
	
	DataConverterFactory factory = DataConverterFactory.create();


	@Test
	public void test() {
		CalendarDateFileHandler testSubject = new CalendarDateFileHandler(blackboard, calendarDateDao, graphDatabase, factory);
		
		try {
			replayAll();
			String current = new java.io.File( "." ).getCanonicalPath();
	        System.out.println("Current dir:"+current);
			assertTrue(testSubject.parse("src/test/resources/data/calendar_dates.txt"));
			verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
