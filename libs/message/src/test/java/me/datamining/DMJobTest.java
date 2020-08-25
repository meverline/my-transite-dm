package me.datamining;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.datamining.shapes.Circle;
import me.datamining.shapes.Rectanlge;
import me.datamining.shapes.Shape;
import me.math.Vertex;

public class DMJobTest {
	
	private BeanTester tester = new BeanTester();
	
	@Test
	public void test()
	{
		Configuration configuration = new ConfigurationBuilder().overrideFactory("shape", new ShapeFactory()).build();

		tester.testBean(DataMiningJob.class, configuration);
	}

	@Test
	public void testCircle() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Circle obj = new Circle();
		Vertex center = new Vertex(35.0, 80.0);
		
		obj.setCenter(center);
		obj.setDistanceInMeters(100);
		
		DataMiningJob job = new DataMiningJob("test", obj);
		
		try {
			String rtn = mapper.writeValueAsString(job);
			assertNotNull(rtn);
			
			DataMiningJob decode = mapper.readValue(rtn, DataMiningJob.class);
			assertNotNull(decode.getShape());
			assertEquals(job.getShape(),decode.getShape());
			assertEquals(job.getName(), decode.getName());
			
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testRectangle() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Vertex left = new Vertex(35.0, 80.0);
		Vertex right = new Vertex(30.0, 90.0);
		
		Rectanlge obj = new Rectanlge(left, right);
		
		DataMiningJob job = new DataMiningJob("test", obj);
		
		try {
			String rtn = mapper.writeValueAsString(job);
			assertNotNull(rtn);
			
			DataMiningJob decode = mapper.readValue(rtn, DataMiningJob.class);
			assertNotNull(decode.getShape());
			assertEquals(job.getShape(),decode.getShape());
			assertEquals(job.getName(), decode.getName());
			
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
	}
	
	public static class ShapeFactory implements Factory<Shape> {


		@Override
		public Shape create() {

			Circle obj = new Circle();
			Vertex center = new Vertex(35.0, 80.0);
			
			obj.setCenter(center);
			obj.setDistanceInMeters(100);
			return obj;
		}
	}

}
