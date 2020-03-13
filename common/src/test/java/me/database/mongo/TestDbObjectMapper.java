package me.database.mongo;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import java.util.Map;

public class TestDbObjectMapper {
	
	TestDocumentDao.TestA objct = new TestDocumentDao.TestA();

	@Test
	public void test() {
		
		try {
			BasicDBObject encoded = DbObjectMapper.encode(objct);
			
			System.out.println("Encoded");
			System.out.println(objct);
			System.out.println();
			Object decode = DbObjectMapper.decode((Map<String,Object>) encoded);
			System.out.println("Decoded:");
			System.out.println(decode);
			System.out.println();
			
			assertTrue(objct.equals(decode));
		} catch ( Exception ex) {
			ex.printStackTrace();
			fail(ex.getLocalizedMessage());
		}
	}

}
