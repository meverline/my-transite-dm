package me.datamining.mapreduce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import me.math.Vertex;

import static org.junit.Assert.assertEquals;

public class TestQueryResults {

	@Test
	public void test() {
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);

		QueryResults testSubject = new QueryResults();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		
		testSubject.startWrite(ps);
		testSubject.write(ps, ul, 10);
		testSubject.write(ps, lr, 20);
		testSubject.endWrite(ps);
		
		assertEquals(550, testSubject.getMaxNumber());
		assertEquals(2, testSubject.getN());
		assertEquals(50.0, testSubject.getVariance(), 0.1);
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		testSubject.read(in, new Handler());
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private class Handler implements ResultsHandler {

		@Override
		public void handleResult(DataResult result) {			
		}
		
	}

}
