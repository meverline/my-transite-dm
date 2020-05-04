package me.crime.database.tuple;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.bson.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

public class TestEndDateTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    private Calendar start = Calendar.getInstance();
    	
	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")		
		EndDateTuple obj = new EndDateTuple(start);		
		
	}
	
	@Test
	public void testGetDoucmentQuery() {
		
		try {
			Document mongo = new Document();
			EndDateTuple obj = new EndDateTuple(start);	
			
			obj.getDoucmentQuery(mongo);
		} catch (Exception ex) {
			assertTrue(true);
		}
				
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
	
		EndDateTuple obj = new EndDateTuple(start);	
		
		obj.getCriterion();
	
	}


}
