package me.crime.database.tuple;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.bson.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;


public class TestCatagoryTuple  extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    private String cats[] = { "one", "two", "three" };
    	
	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")		
		CatagoryTuple obj = new CatagoryTuple(Arrays.asList(cats));		
		
	}
	
	@Test
	public void testGetDoucmentQuery() {
		
		try {
			Document mongo = new Document();
			CatagoryTuple obj = new CatagoryTuple(Arrays.asList(cats));	
			
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
	
		CatagoryTuple obj = new CatagoryTuple(Arrays.asList(cats));	
		
		obj.getCriterion();
	
	}

}
