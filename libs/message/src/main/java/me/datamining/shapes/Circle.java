package me.datamining.shapes;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;

@JsonRootName("Circle")
public class Circle implements Shape{

	private final GeometryFactory factory_  = new GeometryFactory();
	private double distanceInMeters = 1609.3; // 1 mile;
	private Vertex center;
	
	public Circle()
	{
	}
	
	/**
	 * 
	 * @param center
	 */
	public Circle(Vertex center)
	{
		this.center = center;
	}

	/**
	 * 
	 * @param center
	 * @param distanceInMeters
	 */
	public Circle(Vertex center, double distanceInMeters)
	{
		this.distanceInMeters = distanceInMeters;
		this.center = center;
	}
	
	/**
	 * @return the distanceInMeters
	 */
	
	@JsonGetter("distanceInMeters")
	public double getDistanceInMeters() {
		return distanceInMeters;
	}

	/**
	 * @param distanceInMeters the distanceInMeters to set
	 */
	@JsonSetter("distanceInMeters")
	public void setDistanceInMeters(double distanceInMeters) {
		this.distanceInMeters = distanceInMeters;
	}

	/**
	 * @return the center
	 */
	@JsonGetter("center")
	public Vertex getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	@JsonSetter("center")
	public void setCenter(Vertex center) {
		this.center = center;
	}
	
	/**
	 * @return The Radius of the earth at the equator. 
	 */
	private static double equatorialRadiusInMeters()
    {
        return 6378137.0;
    }

	@JsonIgnore
    @Override
    public Geometry shape() {
		double distance = getDistanceInMeters() / Circle.equatorialRadiusInMeters() * 2;
	    GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
	    shapeFactory.setNumPoints(32);
	    shapeFactory.setCentre(new Coordinate(getCenter().getLatitudeDegress(), 
	    									  getCenter().getLongitudeDegress()));
	    
	    shapeFactory.setSize(distance / Circle.equatorialRadiusInMeters() * 2);
	    Polygon circle = shapeFactory.createCircle();
	    	
		List<Coordinate> coords = new ArrayList<Coordinate>();
		for ( Coordinate coord : circle.getCoordinates()) {	
			coords.add( coord );
		}
		
		coords.add(coords.get(0));
		Coordinate array[] = new Coordinate[coords.size()];
		coords.toArray(array);
		
        return factory_.createPolygon(factory_.createLinearRing(array), null);
    }
	
}
