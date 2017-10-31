package me.output;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class KmlFormatter {

	public static void start(StringBuilder kmlFile)
	{
		kmlFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		kmlFile.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
		kmlFile.append("  <Document>\n");
		kmlFile.append("    <Style id=\"JMPPolyStyle\">\n");
		kmlFile.append("	  <LineStyle>\n");
		kmlFile.append("	    <width>1.5</width>\n");
		kmlFile.append("	  </LineStyle>\n");
		kmlFile.append("	  <PolyStyle>\n");
		kmlFile.append("	    <color>3cF03C14</color>\n");
		kmlFile.append("	  </PolyStyle>\n");
		kmlFile.append("    </Style>\n");
		kmlFile.append("    <Style id=\"RPolyStyle\">\n");
		kmlFile.append("	  <LineStyle>\n");
		kmlFile.append("	    <width>1.5</width>\n");
		kmlFile.append("	  </LineStyle>\n");
		kmlFile.append("	  <PolyStyle>\n");
		kmlFile.append("	    <color>3c1400FF</color>\n");
		kmlFile.append("	  </PolyStyle>\n");
		kmlFile.append("    </Style>\n");

	}
	
	public static void end(StringBuilder kmlFile)
	{
		kmlFile.append("  </Document>\n");
		kmlFile.append("</kml>\n");
	}
	
	public static void format(StringBuilder kmlFile, Geometry item)
	{
		if ( item instanceof Polygon ) {
			KmlFormatter.format(kmlFile, item.getCoordinates(), "Polygon");
		}
		else if ( item instanceof Point ) {
			KmlFormatter.format(kmlFile, item.getCoordinate(), "Point");
		}
	}
	
	public static void format(StringBuilder kmlFile, Geometry item, String name)
	{
		if ( item instanceof Point ) {
			KmlFormatter.format(kmlFile, item.getCoordinate(), name);
		}
		else  {
			KmlFormatter.format(kmlFile, item.getCoordinates(), name);
		}
	}
	
	public static void format(StringBuilder kmlFile, Coordinate item, String name)
	{
		kmlFile.append("  <Placemark>\n");
		kmlFile.append("     <name>" + name + "</name>\n");
		kmlFile.append("     <Style>\n");
		kmlFile.append("       <BalloonStyle>\n");
		kmlFile.append("          <displayMode>default</displayMode>\n");
		kmlFile.append("          <bgColor>ff7f1020</bgColor>\n");
		kmlFile.append("       </BalloonStyle>\n");
		kmlFile.append("     </Style>\n");
		kmlFile.append("    <Point>\n");
		kmlFile.append("         <coordinates>");
		kmlFile.append(item.x+", ");
		kmlFile.append(item.y + ", ");
		kmlFile.append(", 0</coordinates>\n");
		kmlFile.append("    </Point>\n");
		kmlFile.append("  </Placemark>\n");
	}
	
	public static void format(StringBuilder kmlFile, Coordinate[] item, String name)
	{
		kmlFile.append("  <Placemark>\n");
		kmlFile.append("    <name>" + name + "</name>\n");
		kmlFile.append("    <styleUrl>#RPolyStyle</styleUrl>\n");
		kmlFile.append("    <visibility>1</visibility>\n");
		kmlFile.append("    <Polygon>\n");
		kmlFile.append("      <tessellate>1</tessellate>\n");
		kmlFile.append("      <altitudeMode>absolute</altitudeMode>\n");

		kmlFile.append("      <outerBoundaryIs>\n");
		kmlFile.append("        <LinearRing>\n");
		kmlFile.append("          <coordinates>\n");

		for ( Coordinate crd : item ) {
			kmlFile.append("              " + crd.x + ", " + crd.y +", 0\n" );
		}

		kmlFile.append("          </coordinates>\n");
		kmlFile.append("        </LinearRing>\n");
		kmlFile.append("      </outerBoundaryIs>\n");
		kmlFile.append("    </Polygon>\n");
		kmlFile.append("  </Placemark>\n");

	}
	
	public static void formatPointsOnly(StringBuilder kmlFile, Coordinate[] item, String name)
	{
		int ndx = 0;
		for ( Coordinate crd : item ) {
			format(kmlFile, crd, name + ndx);
			ndx++;
		}
	}
	
}
