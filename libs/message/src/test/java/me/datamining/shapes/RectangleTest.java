package me.datamining.shapes;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.math.Vertex;

public class RectangleTest {

	@Test
	public void test() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		Vertex left = new Vertex(35.0, 80.0);
		Vertex right = new Vertex(30.0, 90.0);
		
		Rectanlge obj = new Rectanlge(left, right);
		
		try {
			String rtn = mapper.writeValueAsString(obj);
			assertNotNull(rtn);
			
			Rectanlge decode = mapper.readValue(rtn, Rectanlge.class);	
			assertEquals(obj.getUpperLeft(),decode.getUpperLeft());
			assertEquals(obj.getLowerRight(),decode.getLowerRight());
			
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
		
	}

}
