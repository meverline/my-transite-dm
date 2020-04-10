package me.transite.parser.omd;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.transit.parser.omd.FeedsResponse;

public class FeedResponseTest {

	@Test
	public void test() {
		StringBuffer response = new StringBuffer();

		try {
			FileReader input = new FileReader("src/test/resources/getFeeds.json");

			int i;
			while ((i = input.read()) != -1) {
				response.append((char)i);
			}
			input.close();

			FeedsResponse fr = new ObjectMapper().readValue(response.toString(), FeedsResponse.class);
			assertNotNull(fr);
			assertNotNull(fr.getResults().getFeeds());
			assertTrue(true);
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testREal() {
		StringBuffer response = new StringBuffer();

		try {
			FileReader input = new FileReader("src/test/resources/fead_real.json");

			int i;
			while ((i = input.read()) != -1) {
				response.append((char)i);
			}
			
			input.close();
			
			try {
				String val = response.toString().replaceAll("\"u\": \\[\\]", "\"u\": {}");
				FeedsResponse fr = new ObjectMapper().readValue(val, FeedsResponse.class);
				assertNotNull(fr);
				assertNotNull(fr.getResults().getFeeds());
				assertTrue(true);
			} catch (Exception ex ) {
				fail(ex.getLocalizedMessage());
			}
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

}
