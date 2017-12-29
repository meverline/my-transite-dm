package me.database.userTypes;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.Rule;
import org.junit.Test;

import me.database.CSVFieldType;

public class TestCSVClobUsetDataType extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    
    @TestSubject
    private CSVClobUsetDataType testSubject = new CSVClobUsetDataType();
    
	@Mock
	private ResultSet rsMock;
	
	@Mock(type=MockType.NICE)
	private PreparedStatement pmock;
	
	@Mock
	private SharedSessionContractImplementor simock;

	private String names[] = { "name1", "name2" };	
    	
	@Test
	public void test() throws SQLException {
		
		TestData x = new TestData();
		StringBuilder builder = new StringBuilder();
		for ( int ndx = 0; ndx < 5; ndx++) {
			if (ndx > 0 ) { builder.append("\n"); }
			builder.append(x.toCSVLine());
		}
		
		Properties props = new Properties();
		props.setProperty(CSVClobUsetDataType.DEFAULT_CLASS_NAME, TestData.class.getName());
		testSubject.setParameterValues(props);
		
		testSubject.deepCopy(names);
		testSubject.equals(names, names);
		testSubject.equals(names, builder.toString());
		testSubject.hashCode(builder.toString());
		testSubject.returnedClass();
		assertNotNull(testSubject.sqlTypes());
		assertFalse(testSubject.isMutable());
		
		testSubject.replace(builder.toString(), builder.toString(), null);
	}


	@Test
	public void testNullSafeGet() throws SQLException {
	
		Properties props = new Properties();
		props.setProperty(CSVClobUsetDataType.DEFAULT_CLASS_NAME, TestData.class.getName());
		testSubject.setParameterValues(props);

		TestData x = new TestData();
		StringBuilder builder = new StringBuilder();
		for ( int ndx = 0; ndx < 5; ndx++) {
			if (ndx > 0 ) { builder.append("\n"); }
			builder.append(x.toCSVLine());
		}
			
		expect(rsMock.getString(EasyMock.anyString())).andReturn(builder.toString());
		replayAll();
		testSubject.nullSafeGet(rsMock, names, null, null);
		verifyAll();
		resetAll();
		
		expect(rsMock.getString(EasyMock.anyString())).andReturn(null);
		replayAll();
		testSubject.nullSafeGet(rsMock, names, null, null);
		verifyAll();
		resetAll();
		
		expect(rsMock.getString(EasyMock.anyString())).andReturn("");
		replayAll();
		testSubject.nullSafeGet(rsMock, names, null, null);
		verifyAll();
		resetAll();
		
		testSubject.assemble(builder.toString(), null);
		try {
			testSubject.assemble(names, null);
			fail("fail");
		} catch (Exception ex) {
			assertTrue(true);
		}
		
		testSubject.nullSafeGet(rsMock, names, simock, null);
	}
	
	@Test
	public void testNullSafeSet() throws SQLException {
		
		Properties props = new Properties();
		props.setProperty(CSVClobUsetDataType.DEFAULT_CLASS_NAME, TestData.class.getName());
		testSubject.setParameterValues(props);

		List<TestData> builder = new ArrayList<>();
		builder.add(new TestData());
		builder.add(new TestData());
		builder.add(new TestData());
			
		testSubject.nullSafeSet(pmock, builder, 0, null);
		testSubject.nullSafeSet(pmock, null, 0, null);
		
		try {
			testSubject.nullSafeSet(pmock, "myself", 0, null);
			fail("fail");
		} catch (Exception e){
			assertTrue(true);
		}
		
		testSubject.disassemble(builder);
		testSubject.disassemble(names);
		
		testSubject.nullSafeSet(pmock, null, 0, simock);

	}

	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	public static class TestData implements CSVFieldType {
		
		private String field1 = "one";
		private String field2 = "two";

		@Override
		public String toCSVLine() {
			StringBuilder builder = new StringBuilder();
			
			builder.append(field1);
			builder.append(CSVFieldType.COMMA);
			builder.append(field2);
			return builder.toString();
		}

		@Override
		public void fromCSVLine(String line) {
		}
		
	}

}
