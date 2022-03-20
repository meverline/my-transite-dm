package me.datamining.shapes;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import me.transit.dao.query.SpatialQuery;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;
import org.locationtech.jts.geom.Point;

@Jacksonized
@Data
public class Polygon implements Shape{

	private List<Vertex> coordinates;
	
	public Polygon() {
	}

	/**
	 * 
	 * @param coordinates
	 */
	public Polygon(List<Vertex> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public void setQueryShape(SpatialQuery query) {
		List<Point> pointList = new ArrayList<>();
		for (Vertex v : this.getCoordinates()) {
			pointList.add(v.toPoint());
		}
		query.addPolygonConstraint(pointList);
	}

	@Override
	public Vertex getUpperLeft() {
		double minWest = -90;
		double maxNorth = 180.0;

		for (Vertex v : this.getCoordinates()) {
			minWest = Math.min(minWest, v.getLatitudeDegress());
			maxNorth = Math.max(maxNorth, v.getLongitudeDegress());
		}
		return new Vertex(maxNorth, minWest);
	}

	public Vertex getLowerRight() {
		double minWest = 90;
		double maxNorth = -180.0;

		for (Vertex v : this.getCoordinates()) {
			minWest = Math.max(minWest, v.getLatitudeDegress());
			maxNorth = Math.min(maxNorth, v.getLongitudeDegress());
		}
		return new Vertex(maxNorth, minWest);
	}
}
