package me.output;

import java.io.IOException;
import java.io.PrintStream;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class KmlOutput {

	private PrintStream kmlFile = null;
	private StringBuilder kml = new StringBuilder();
	
	public void open(String file) throws IOException
	{
		close();
		kmlFile = new PrintStream(file);
		kml.delete(0, kml.length());
		KmlFormatter.start(kml);
		kmlFile.println(kml);
	}
	
	public void close() throws IOException
	{
		if (kmlFile != null) {
			kml.delete(0, kml.length());
			KmlFormatter.end(kml);
			kmlFile.println(kml);
			kmlFile.close();
		}
	}
		
	public void write(Point pt, String name)
	{
		kml.delete(0, kml.length());
		KmlFormatter.format(kml, pt, name);
		kmlFile.println(kml);	
	}
	
	public void write(Geometry pt, String name)
	{
		kml.delete(0, kml.length());
		KmlFormatter.format(kml, pt, name);
		kmlFile.println(kml);
	}
	
	
}
