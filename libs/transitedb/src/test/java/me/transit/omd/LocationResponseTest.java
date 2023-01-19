package me.transit.omd;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
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
			Assert.assertNotNull(fr);
			Assert.assertNotNull(fr.getResults().getLocations());
			Assert.assertTrue(true);
		} catch (IOException e) {
			Assert.fail(e.getLocalizedMessage());
		}
	
	}

}
