package me.datamining.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import me.transit.dao.query.SpatialQuery;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import me.math.Vertex;

@Jacksonized
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

	@Override
	public void setQueryShape(SpatialQuery query) {
		query.addCircleConstriant(this.getCenter().toPoint(), this.getDistanceInMeters());
	}
}
