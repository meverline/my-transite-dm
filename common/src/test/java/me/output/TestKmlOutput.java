package me.output;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import me.math.Vertex;
import me.math.kdtree.MinBoundingRectangle;

public class TestKmlOutput {

	@Test
	public void test() throws IOException {
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		MinBoundingRectangle obj = new MinBoundingRectangle();
		obj = new MinBoundingRectangle(ul);
		obj.extend(lr);
		
		File fp = File.createTempFile("test", "tmp");
		fp.deleteOnExit();
		
		KmlOutput testSubject = new KmlOutput();
		
		testSubject.open(fp.toString());
		testSubject.write(ul.toPoint(), "testPoint");
		testSubject.write(obj.toPolygon(), "polygon");
		
		testSubject.close();
	}

}
