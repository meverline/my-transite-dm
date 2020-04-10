package me.transit.omd;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.transit.omd.data.LocationsResponse;

public class LocationResponseTest {

	@Test
	public void test() {
		StringBuffer response = new StringBuffer();

		try {
			FileReader input = new FileReader("src/test/resources/getLocations.json");

			int i;
			while ((i = input.read()) != -1) {
				response.append((char)i);
			}
			input.close();

			LocationsResponse fr = new ObjectMapper().readValue(response.toString(), LocationsResponse.class);
			assertNotNull(fr);
			assertNotNull(fr.getResults().getLocations());
			assertTrue(true);
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	
	}

}
