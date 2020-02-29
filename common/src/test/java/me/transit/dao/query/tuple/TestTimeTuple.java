package me.transit.dao.query.tuple;

import static org.easymock.EasyMock.expect;

import java.util.Calendar;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.BasicDBObject;

public class TestTimeTuple extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    	
	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")
		TimeTuple obj = new TimeTuple("field", Calendar.getInstance(), Calendar.getInstance());		
		obj = new TimeTuple(String.class, "field", Calendar.getInstance(), Calendar.getInstance());
			
	}
	
	@Test
	public void testGetDoucmentQuery() {
		
		BasicDBObject mongo = new BasicDBObject();
		TimeTuple obj = new TimeTuple("field", Calendar.getInstance(), Calendar.getInstance());
		obj.getDoucmentQuery(mongo);			
	}
	
	@Test
	public void testGetCriterion() {
		
		Criteria mongo = this.createNiceMock(Criteria.class);
		
		expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
		expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
		replayAll();
		
		TimeTuple obj = new TimeTuple("field", Calendar.getInstance(), Calendar.getInstance());
		obj.getCriterion();
		
		obj = new TimeTuple(String.class, "field", Calendar.getInstance(), Calendar.getInstance());
		obj.getCriterion();
	}

}
