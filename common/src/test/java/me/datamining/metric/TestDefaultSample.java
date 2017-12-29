package me.datamining.metric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Rule;
import org.junit.Test;

public class TestDefaultSample extends EasyMockSupport {
	
    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
	@Mock
	private IDataProvider stop;

	@Test
	public void test() {
		DefaultSample obj = new DefaultSample();
		
		replayAll();
		assertEquals(0, obj.getMetric(stop), 0.01);
		verifyAll();
		resetAll();
		
		assertTrue(obj.isDataListEmpty());
		
		for ( int ndx = 1; ndx < 21; ndx++) {
			obj.addSampleData(new DataProvider(ndx));
		}
		
		for ( int ndx = 1; ndx < 21; ndx++) {
			obj.addSampleData(new DataProvider(ndx));
		}
		
		obj.addSampleData("Thiis is String");
		
		assertFalse(obj.isDataListEmpty());
		assertEquals(20, obj.getNumber(), 0.01);
		assertNotNull(obj.getDataList());
		
	}
	
	///////////////////////////////////////////////////////
	
	private class DataProvider implements IDataProvider {

		private long id;
		public DataProvider(long id) {
			this.id = id;
		}
		
		@Override
		public long getUUID() {
			return this.id;
		}
		
	}
	
	

}
