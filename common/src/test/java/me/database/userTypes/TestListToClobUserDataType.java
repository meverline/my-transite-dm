package me.database.userTypes;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.Rule;
import org.junit.Test;
import static org.easymock.EasyMock.expect;

public class TestListToClobUserDataType extends EasyMockSupport {

	@Rule
	public EasyMockRule rule = new EasyMockRule(this);

	@TestSubject
	private ListToClobUserDataType testSubject = new ListToClobUserDataType();

	@Mock
	private ResultSet rsMock;
	
	@Mock(type=MockType.NICE)
	private PreparedStatement pmock;
	
	@Mock
	private SharedSessionContractImplementor simock;

	private String testData[] = { "One", "Two", "Three", "Four", "Five" };

	private String names[] = { "name1", "name2" };
	
	@Test
	public void test() throws SQLException {
		
		StringBuilder builder = new StringBuilder();
		for ( String item : testData) {
			if (builder.length() > 0 ) { builder.append("\n"); }
			builder.append(item);
		}
		
		testSubject.deepCopy(names);
		testSubject.equals(names, names);
		testSubject.equals(names, builder.toString());
		testSubject.hashCode(builder.toString());
		assertNotNull(testSubject.returnedClass());
		assertNotNull(testSubject.sqlTypes());
		assertFalse(testSubject.isMutable());
		
		testSubject.replace(builder.toString(), builder.toString(), null);
	}


	@Test
	public void testNullSafeGet() throws SQLException {
		
		StringBuilder builder = new StringBuilder();
		for ( String item : testData) {
			if (builder.length() > 0 ) { builder.append("\n"); }
			builder.append(item);
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
		testSubject.assemble(names, null);
		
		testSubject.nullSafeGet(rsMock, names, simock, null);
	}
	
	@Test
	public void testNullSafeSet() throws SQLException {
		
		HashSet<String> builder = new HashSet<String>();
		for ( String item : testData) {
			builder.add(item);
		}
			
		testSubject.nullSafeSet(pmock, builder, 0, null);
		testSubject.nullSafeSet(pmock, null, 0, null);
		
		try {
			testSubject.nullSafeSet(pmock, new ArrayList<String>(), 0, null);
			fail("fail");
		} catch (Exception e){
			assertTrue(true);
		}
		
		testSubject.disassemble(builder);
		testSubject.disassemble(names);
		
		testSubject.nullSafeSet(pmock, null, 0, simock);

	}

}
