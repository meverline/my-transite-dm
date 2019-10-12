package me.database.mongo;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;


@SuppressWarnings("deprecation")
public class TestDocumentDao extends EasyMockSupport{

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
   
	@Mock(type=MockType.NICE)
	private Mongo writer;
	
	@Mock(type=MockType.NICE)
	private DB dbmock;
	
	@Mock(type=MockType.NICE)
	private DBCollection collection;
	
	
	private void setUpMock() {
		expect(writer.getDB(EasyMock.anyString())).andReturn(dbmock).anyTimes();
		expect(dbmock.getCollection(EasyMock.anyString())).andReturn(collection).anyTimes();
		expect(collection.count()).andReturn(new Long(3)).anyTimes();
		
		replayAll();
	}

	@Test
	public void test() throws UnknownHostException {
		
		this.setUpMock();

		IDocumentDao doc = DocumentDao.instance(writer);
		assertNotNull(doc);
		assertEquals(3, doc.size());
		assertEquals(3, doc.size("unk"));
		
		doc = DocumentDao.instance(writer);
		assertNotNull(doc);
		
		verifyAll();
		resetAll();
	}
	
	@Test
	public void testPrimative() throws UnknownHostException {
		
		this.setUpMock();
		DocumentDao doc = DocumentDao.class.cast(DocumentDao.instance(writer));
		
		Class<?> info[] = {
				String.class,
				Long.class,
				Integer.class,
				Double.class,
				Boolean.class,
				Float.class,
				Short.class,
				Character.class,
				Byte.class
		};
		
		for (Class<?> item : info ) {
			assertTrue(doc.isPrimativeType(item));
		}
		
		assertFalse(doc.isPrimativeType(getClass() ));
		
		verifyAll();
		resetAll();
	}
	
	@Test
	public void testSkip() throws UnknownHostException {
		
		this.setUpMock();
		DocumentDao doc = DocumentDao.class.cast(DocumentDao.instance(writer));
		
		String info[] = {
				"_id",
				"@class"
		};
		
		for (String field : info ) {
			assertTrue(doc.skipField(field));
		}
		
		assertFalse(doc.skipField("field"));
		
		verifyAll();
		resetAll();
	}
	
	@Test
	public void testToDocField() throws UnknownHostException {
				
		String info[] = {
				"_id",
				"@class",
				"field1",
				"field2",
				"field3",
				"field4",
		};
				
		assertNotNull( DocumentDao.toDocField(Arrays.asList(info)));
	}
	
	@Test
	public void testAdd() throws UnknownHostException {
				
		this.setUpMock();
		DocumentDao doc = DocumentDao.class.cast(DocumentDao.instance(writer));
		
		TestA objct = new TestA();
		
		doc.add(objct);
		
		doc.add(objct.toDocument());
		
		@SuppressWarnings("unused")
		Object obj = doc.translateToDbObject(objct.toDocument());
		
		verifyAll();
		resetAll();

	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public static class TestB implements IDocument {
		
		private String name = "Name";
		private String array[] = { "a", "b", "c", "d" };
		private List<String> data = Arrays.asList(array);

		@Override
		public Map<String, Object> toDocument() {
			Map<String,Object> rtn = new HashMap<String,Object>();

            rtn.put(IDocument.CLASS, TestA.class.getName());
            rtn.put("name", this.getName());
            rtn.put("array", this.getArray());
            rtn.put("data", this.getData());
            rtn.put("item", null);
			return rtn;
		}

		@Override
		public void handleEnum(String key, Object value) {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String[] getArray() {
			return array;
		}

		public void setArray(String[] array) {
			this.array = array;
		}

		public List<String> getData() {
			return data;
		}

		public void setData(List<String> data) {
			this.data = data;
		}
		
		public String getItem() {
			return name;
		}

		public void setItem(String name) {
		}
	}

	public static class TestA implements IDocument {
		
		private String name = "Name";
		private int min = 20;
		private double value = 40.0;
		private TestB object = new TestB();

		@Override
		public Map<String, Object> toDocument() {
			Map<String,Object> rtn = new HashMap<String,Object>();

            rtn.put(IDocument.CLASS, TestA.class.getName());
            rtn.put("name", this.getName());
            rtn.put("min", this.getMin());
            rtn.put("value", this.getValue());
            rtn.put("object", this.getObject());
			return rtn;
		}

		@Override
		public void handleEnum(String key, Object value) {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public TestB getObject() {
			return object;
		}

		public void setObject(TestB object) {
			this.object = object;
		}
		
	}

}
