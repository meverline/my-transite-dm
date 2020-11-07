package me.crime.database.tuple;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.easymock.EasyMock.expect;


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
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
	
		CatagoryTuple obj = new CatagoryTuple(Arrays.asList(cats));	
		
		obj.getCriterion();
	
	}

}
