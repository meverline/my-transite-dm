package me.output;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class TestKmlOutput {

	@Test
	public void test() throws IOException {
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

		File fp = File.createTempFile("test", "tmp");
		fp.deleteOnExit();
		
		KmlOutput testSubject = new KmlOutput();
		
		testSubject.open(fp.toString());
		testSubject.write(ul, "testPoint");
		testSubject.write(obj, "polygon");
		
		testSubject.close();
	}

}
