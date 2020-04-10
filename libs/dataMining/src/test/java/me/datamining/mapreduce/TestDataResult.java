package me.datamining.mapreduce;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import me.math.Vertex;

public class TestDataResult {

	@Test
	public void test() {
		
		BeanTester tester = new BeanTester();
		
		Configuration configuration = new ConfigurationBuilder().
				overrideFactory("point", new VertexFactory()).build();
		
		tester.testBean(DataResult.class, configuration);
		
		Vertex ul = new Vertex(38.941, -77.286);
		
		DataResult testSubject = new DataResult(ul, 200);
		testSubject.reset();
		
		testSubject = new DataResult(testSubject);
	}
	
	//////////////////////////////////////////////////////////////////////////////

	class VertexFactory implements Factory<Vertex> {
		
        @Override
        public Vertex create() {
        	    return new Vertex(38.941, -77.286);
        }
    }
}
