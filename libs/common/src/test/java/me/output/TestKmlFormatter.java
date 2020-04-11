package me.output;

import static org.junit.Assert.*;

import org.junit.Test;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class TestKmlFormatter {

	@Test
	public void test() {

		GeometryFactory factory_  = new GeometryFactory();
		Point ul = factory_.createPoint(new Coordinate(38.941, -77.286));
		Point lr = factory_.createPoint(new Coordinate(38.827, -77.078));

		Coordinate [] coords = new Coordinate[5];

		coords[0] = new Coordinate(38.941, 77.078);
		coords[1] = new Coordinate(38.941, -77.286);
		coords[2] = new Coordinate(38.827, -77.286);
		coords[3] = new Coordinate(38.827, -77.078);
		coords[4] = coords[0];
		Polygon obj = factory_.createPolygon(factory_.createLinearRing(coords), null);

		StringBuilder kmlFile = new StringBuilder();
		KmlFormatter.start(kmlFile);
		assertTrue(kmlFile.length() > 0);
		KmlFormatter.format(kmlFile, ul);
		KmlFormatter.format(kmlFile, obj);
		
		KmlFormatter.formatPointsOnly(kmlFile, obj.getCoordinates(), "test");
		KmlFormatter.end(kmlFile);
		assertTrue( kmlFile.toString().contains("</kml>"));
		
	}

}
