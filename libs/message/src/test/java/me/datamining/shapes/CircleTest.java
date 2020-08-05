package me.datamining.shapes;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.math.Vertex;

public class CircleTest {

	@Test
	public void test() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Circle obj = new Circle();
		Vertex center = new Vertex(35.0, 80.0);
		
		obj.setCenter(center);
		obj.setDistanceInMeters(100);
		
		try {
			String rtn = mapper.writeValueAsString(obj);
			assertNotNull(rtn);
			
			Circle decode = mapper.readValue(rtn, Circle.class);	
			assertEquals(obj.getDistanceInMeters(), decode.getDistanceInMeters(), 0.001);
			assertEquals(obj.getCenter(),decode.getCenter());
			
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
		
	}

}
