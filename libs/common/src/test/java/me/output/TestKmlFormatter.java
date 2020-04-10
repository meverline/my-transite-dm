package me.output;

import static org.junit.Assert.*;

import org.junit.Test;

import me.math.Vertex;
import me.math.kdtree.MinBoundingRectangle;

public class TestKmlFormatter {

	@Test
	public void test() {
		
		Vertex ul = new Vertex(38.941, -77.286);
		Vertex lr = new Vertex(38.827, -77.078);
		
		MinBoundingRectangle obj = new MinBoundingRectangle();
		obj = new MinBoundingRectangle(ul);
		obj.extend(lr);
		
		StringBuilder kmlFile = new StringBuilder();
		KmlFormatter.start(kmlFile);
		assertTrue(kmlFile.length() > 0);
		KmlFormatter.format(kmlFile, ul.toPoint());
		KmlFormatter.format(kmlFile, obj.toPolygon());
		
		KmlFormatter.formatPointsOnly(kmlFile, obj.toPolygon().getCoordinates(), "test");
		KmlFormatter.end(kmlFile);
		assertTrue( kmlFile.toString().contains("</kml>"));
		
	}

}
