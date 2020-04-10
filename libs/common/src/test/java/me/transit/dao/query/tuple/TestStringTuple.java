package me.transit.dao.query.tuple;

import static org.easymock.EasyMock.expect;

import org.bson.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.BasicDBObject;

public class TestStringTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    	
	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")
		StringTuple obj = new StringTuple("field", "value", StringTuple.MATCH.CONTAINS);		
		obj = new StringTuple(String.class, "field", "value", StringTuple.MATCH.CONTAINS);
	
		for ( StringTuple.MATCH logic : StringTuple.MATCH.values()) {
			obj = new StringTuple( "field", "value", logic);
		}
		
	}
	
	@Test
	public void testGetDoucmentQuery() {

		Document mongo = new Document();
		for ( StringTuple.MATCH logic : StringTuple.MATCH.values()) {
			StringTuple obj = new StringTuple( "field", "value", logic);
			obj.getDoucmentQuery(mongo);
		}
				
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
	
		for ( StringTuple.MATCH logic : StringTuple.MATCH.values()) {
			StringTuple obj = new StringTuple( String.class, "field", "value", logic);
			
			obj.getCriterion();
		}
	
		StringTuple obj = new StringTuple( "field", "100", StringTuple.MATCH.CONTAINS);
		obj.getCriterion();
	  
	}

}
