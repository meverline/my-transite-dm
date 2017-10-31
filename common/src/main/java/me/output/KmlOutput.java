package me.output;

import java.io.IOException;
import java.io.PrintStream;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class KmlOutput {

	private PrintStream kmlFile = null;
	
	public void open(String file) throws IOException
	{
		close();
		kmlFile = new PrintStream(file);
		header();
	}
	
	public void close() throws IOException
	{
		if (kmlFile != null) {
			footer();
			kmlFile.close();
		}
	}
	
	public void header()
	{
		kmlFile.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		kmlFile.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
		kmlFile.println("<Document>");
		kmlFile.println("<Style id=\"JMPPolyStyle\">");
		kmlFile.println("	<LineStyle>");
		kmlFile.println("	   <width>1.5</width>");
		kmlFile.println("	</LineStyle>");
		kmlFile.println("	<PolyStyle>");
		kmlFile.println("	   <color>3cF03C14</color>");
		kmlFile.println("	</PolyStyle>");
		kmlFile.println("</Style>");
		kmlFile.println("<Style id=\"RPolyStyle\">");
		kmlFile.println("	<LineStyle>");
		kmlFile.println("	   <width>1.5</width>");
		kmlFile.println("	</LineStyle>");
		kmlFile.println("	<PolyStyle>");
		kmlFile.println("	   <color>3c1400FF</color>");
		kmlFile.println("	</PolyStyle>");
		kmlFile.println("</Style>");
	}

	public void footer()
	{
		kmlFile.println("</kml>");
	}
	
	public void write(Point pt, String name)
	{
		kmlFile.println("  <Placemark>");
		kmlFile.println("     <name>" +  name  + "</name>");
		kmlFile.println("     <Style>");
		kmlFile.println("       <BalloonStyle>");
		kmlFile.println("          <displayMode>default</displayMode>");
		kmlFile.println("          <bgColor>ff7f1020</bgColor>");
		kmlFile.println("       </BalloonStyle>");
		kmlFile.println("     </Style>");
		kmlFile.println("    <Point>");
		kmlFile.print("         <coordinates>");
		kmlFile.print( pt.getCoordinate().y + ", ");
		kmlFile.print( pt.getCoordinate().x + ", ");
		kmlFile.println(", 0</coordinates>");
		kmlFile.println("    </Point>");
		kmlFile.println("  </Placemark>");		
	}
	
	public void write(Geometry pt, String name)
	{
		kmlFile.println("  <Placemark>");
		kmlFile.println("    <name>" +  name  + "</name>");
		kmlFile.println("    <styleUrl>#RPolyStyle</styleUrl>");
		kmlFile.println("    <LineString>");

		for ( Coordinate crd : pt.getCoordinates() ) {
			kmlFile.print("         <coordinates>");
			kmlFile.print( crd.y + ", ");
			kmlFile.print( crd.x + ", ");
			kmlFile.println(", 0</coordinates>");
		}
		kmlFile.println("    </LineString>");
		kmlFile.println("  </Placemark>");		
	}
	
	
}
