package me.datamining.shapes;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.math.Vertex;

public class PolygonTest {

	@Test
	public void test() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		Vertex left = new Vertex(35.0, 80.0);
		Vertex right = new Vertex(30.0, 90.0);
		
		List<Vertex> shape = new ArrayList<>();
		shape.add(left);
		shape.add(right);
		shape.add(left);
		
		Polygon obj = new Polygon(shape);
		
		try {
			String rtn = mapper.writeValueAsString(obj);
			assertNotNull(rtn);
			
			Polygon decode = mapper.readValue(rtn, Polygon.class);	
			for ( int ndx =0; ndx < obj.getCoordinates().size(); ndx++) {
				assertEquals(obj.getCoordinates().get(ndx),decode.getCoordinates().get(ndx));
			}
			
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
	}

}
