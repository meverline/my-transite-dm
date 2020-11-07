package me.crime.database.tuple;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import static org.easymock.EasyMock.expect;

public class TestStartDateTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    private Calendar start = Calendar.getInstance();
    	
	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")		
		StartDateTuple obj = new StartDateTuple(start);		
		
	}

	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
	
		StartDateTuple obj = new StartDateTuple(start);	
		
		obj.getCriterion();
	
	}


}
