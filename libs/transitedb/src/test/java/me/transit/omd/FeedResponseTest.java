package me.transit.omd;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.transit.omd.data.FeedsResponse;

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
			Assert.assertNotNull(fr);
			Assert.assertNotNull(fr.getResults().getFeeds());
			Assert.assertTrue(true);
		} catch (IOException e) {
			Assert.fail(e.getLocalizedMessage());
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
				Assert.assertNotNull(fr);
				Assert.assertNotNull(fr.getResults().getFeeds());
				Assert.assertTrue(true);
			} catch (Exception ex ) {
				Assert.fail(ex.getLocalizedMessage());
			}
		} catch (IOException e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

}
