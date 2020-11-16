package me.database.mongo;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import me.database.nsstore.AbstractDocument;
import me.database.nsstore.IDocumentSession;
import org.bson.Document;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class TestDocumentDao extends EasyMockSupport{

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
   
	@Mock(type=MockType.NICE)
	private MongoClient writer;
	
	@Mock(type=MockType.NICE)
	private MongoDatabase dbmock;
	
	@Mock(type=MockType.NICE)
	private MongoCollection<Document> collection;
	
	
	private void setUpMock() {
		expect(writer.getDatabase(EasyMock.anyString())).andReturn(dbmock).anyTimes();
		expect(dbmock.getCollection(EasyMock.anyString())).andReturn(collection).anyTimes();
		expect(collection.countDocuments()).andReturn(new Long(3)).anyTimes();
		
		replayAll();
	}

	@Test
	public void test() throws UnknownHostException {
		
		this.setUpMock();

		IDocumentSession doc = MongoDocumentSession.instance(writer);
		assertNotNull(doc);
		assertEquals(3, doc.size());
		assertEquals(3, doc.size("unk"));
		
		doc = MongoDocumentSession.instance(writer);
		assertNotNull(doc);
		
		verifyAll();
		resetAll();
	}
	
	@Test
	public void testSkip() throws UnknownHostException {
		
		this.setUpMock();
		MongoDocumentSession doc = MongoDocumentSession.class.cast(MongoDocumentSession.instance(writer));
		
		String [] info = {
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
				
		String [] info = {
				"_id",
				"@class",
				"field1",
				"field2",
				"field3",
				"field4",
		};
				
		assertNotNull( MongoDocumentSession.toDocField(Arrays.asList(info)));
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public static class TestB extends AbstractDocument {
		
		private String name = "Name";
		private String [] array = { "a", "b", "c", "d" };
		private List<String> data = Arrays.asList(array);

		@JsonGetter("TestB.name")
		public String getName() {
			return name;
		}

		@JsonSetter("TestB.name")
		public void setName(String name) {
			this.name = name;
		}

		@JsonGetter("TestB.array")
		public String[] getArray() {
			return array;
		}

		@JsonSetter("TestB.array")
		public void setArray(String[] array) {
			this.array = array;
		}

		@JsonGetter("TestB.data")
		public List<String> getData() {
			return data;
		}

		@JsonSetter("TestB.data")
		public void setData(List<String> data) {
			this.data = data;
		}
		
		@JsonGetter("TestB.item")
		public String getItem() {
			return name;
		}

		@JsonSetter("TestB.item")
		public void setItem(String name) {
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(array);
			result = prime * result + ((data == null) ? 0 : data.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestB other = (TestB) obj;
			if (!Arrays.equals(array, other.array))
				return false;
			if (data == null) {
				if (other.data != null)
					return false;
			} else if (!data.equals(other.data))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TestB [name=" + name + ", array=" + Arrays.toString(array) + ", data=" + data + "]";
		}
		
		
	}

	public static class TestA extends AbstractDocument {
		
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
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + min;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((object == null) ? 0 : object.hashCode());
			long temp;
			temp = Double.doubleToLongBits(value);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestA other = (TestA) obj;
			if (min != other.min)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (object == null) {
				if (other.object != null)
					return false;
			} else if (!object.equals(other.object))
				return false;
			if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TestA [name=" + name + ", min=" + min + ", value=" + value + ", object=" + object + "]";
		}

	}

}
