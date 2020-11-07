package me.crime.database.tuple;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import static org.easymock.EasyMock.expect;

public class TestAddressTuple extends EasyMockSupport {

	    @Rule
	    public EasyMockRule rule = new EasyMockRule(this);
	    	
		@Test
		public void testConstructor() {
			@SuppressWarnings("unused")		
			AddressTuple obj = new AddressTuple("3329 Caddy drive");		
			
		}

		
		@Test
		public void testGetCriterion() {
			
			Criteria mongo = this.createNiceMock(Criteria.class);
			
			expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
			expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
			replayAll();
		
			AddressTuple obj = new AddressTuple("3329 Caddy drive");	
			
			obj.getCriterion();
		
		}


}
