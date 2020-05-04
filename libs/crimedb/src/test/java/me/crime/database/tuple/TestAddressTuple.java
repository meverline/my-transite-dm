package me.crime.database.tuple;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;

import org.bson.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

public class TestAddressTuple extends EasyMockSupport {

	    @Rule
	    public EasyMockRule rule = new EasyMockRule(this);
	    	
		@Test
		public void testConstructor() {
			@SuppressWarnings("unused")		
			AddressTuple obj = new AddressTuple("3329 Caddy drive");		
			
		}
		
		@Test
		public void testGetDoucmentQuery() {
			
			try {
				Document mongo = new Document();
				AddressTuple obj = new AddressTuple("3329 Caddy drive");	
				
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
		
			AddressTuple obj = new AddressTuple("3329 Caddy drive");	
			
			obj.getCriterion();
		
		}


}
