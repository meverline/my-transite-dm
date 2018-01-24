package me.datamining.metric;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;

import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;

public class TestTransitStopSpatialSample extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

	@Test
	public void test() {
		
		TransitStopSpatialSample testSubject = partialMockBuilder(TransitStopSpatialSample.class) //create builder first
		         .addMockedMethod("getRoutes") // tell EasyMock to mock foo() method
		         .createMock(); 
	
		TransitStop stop = new TransitStop();
		List<RouteStopData> list = new ArrayList<RouteStopData>();
		
		for (int ndx = 0; ndx < 5; ndx++ ) {
			list.add(new RouteStopData());			
		}
		
		expect(testSubject.getRoutes(EasyMock.anyObject(TransitStop.class))).andReturn(list);
	    replayAll();
		assertEquals(5.0, testSubject.getMetric(stop), 0.01);
		verifyAll();
		resetAll();
		
		expect(testSubject.getRoutes(EasyMock.anyObject(TransitStop.class))).andReturn(null);
	    replayAll();
		assertEquals(0.0, testSubject.getMetric(stop), 0.01);
		verifyAll();
		resetAll();
		
		expect(testSubject.getRoutes(EasyMock.anyObject(TransitStop.class))).andReturn(list);
	    replayAll();
		assertEquals(0.0, testSubject.getMetric(null), 0.01);
		verifyAll();
		resetAll();
		
		
	}

}
