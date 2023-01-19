package me.datamining.shapes;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import me.math.Vertex;

@Data
public class Circle implements Shape{

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

	protected static double equatorialRadiusInMeters()
	{
		return 6378137.0;
	}

	private List<Vertex> createCirecle() {
		Point location = this.getCenter().toPoint();
		@SuppressWarnings("unused")
		double distance = this.getDistanceInMeters() / Circle.equatorialRadiusInMeters() * 2;
		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(32);
		shapeFactory.setCentre(new Coordinate(location.getX(), location.getY()));
		shapeFactory.setSize(this.getDistanceInMeters() / Circle.equatorialRadiusInMeters() * 2);
		Polygon circle = shapeFactory.createCircle();

		List<Vertex> coords = new ArrayList<>();
		for ( Coordinate coord : circle.getCoordinates()) {
			coords.add( new Vertex(coord.getX(), coord.getY()) );
		}

		coords.add(coords.get(0));
		return coords;
	}

	@JsonIgnore
	public Vertex getUpperLeft() {

		double minWest = -90;
		double maxNorth = 180.0;

		for (Vertex v : this.createCirecle() ) {
			minWest = Math.min(minWest, v.getLatitudeDegress());
			maxNorth = Math.max(maxNorth, v.getLongitudeDegress());
		}
		return new Vertex(maxNorth, minWest);
	}

	@JsonIgnore
	public Vertex getLowerRight() {
		double minWest = 90;
		double maxNorth = -180.0;

		for (Vertex v : this.createCirecle()) {
			minWest = Math.max(minWest, v.getLatitudeDegress());
			maxNorth = Math.min(maxNorth, v.getLongitudeDegress());
		}
		return new Vertex(maxNorth, minWest);
	}

}
