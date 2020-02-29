package me.database.mongo;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;


@SuppressWarnings("deprecation")
public class TestDocumentDao extends EasyMockSupport{

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
   
	@Mock(type=MockType.NICE)
	private MongoClient writer;
	
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
			assertTrue(DbObjectMapper.isPrimativeType(item));
		}
		
		assertFalse(DbObjectMapper.isPrimativeType(getClass() ));
		
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
		
		doc.add(objct);
		
		try {
			Object obj = DbObjectMapper.encode(objct);
		} catch (Exception ex) {
			
		}
		
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

		@JsonGetter("name")
		public String getName() {
			return name;
		}

		@JsonSetter("name")
		public void setName(String name) {
			this.name = name;
		}

		@JsonGetter("array")
		public String[] getArray() {
			return array;
		}

		@JsonSetter("array")
		public void setArray(String[] array) {
			this.array = array;
		}

		@JsonGetter("data")
		public List<String> getData() {
			return data;
		}

		@JsonSetter("data")
		public void setData(List<String> data) {
			this.data = data;
		}
		
		@JsonGetter("item")
		public String getItem() {
			return name;
		}

		@JsonSetter("item")
		public void setItem(String name) {
		}
	}

	public static class TestA implements IDocument {
		
		private String name = "Name";
		private int min = 20;
		private double value = 40.0;
		private TestB object = new TestB();


		@JsonGetter("name")
		public String getName() {
			return name;
		}

		@JsonSetter("name")
		public void setName(String name) {
			this.name = name;
		}

		@JsonGetter("min")
		public int getMin() {
			return min;
		}

		@JsonSetter("min")
		public void setMin(int min) {
			this.min = min;
		}

		@JsonGetter("value")
		public double getValue() {
			return value;
		}

		@JsonSetter("value")
		public void setValue(double value) {
			this.value = value;
		}

		@JsonGetter("object")
		public TestB getObject() {
			return object;
		}

		@JsonSetter("object")
		public void setObject(TestB object) {
			this.object = object;
		}
		
	}

}
