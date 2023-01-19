package me.datamining.shapes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import me.math.Vertex;

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
	@JsonIgnore
	public Vertex getUpperLeft() {
		double minWest = -90;
		double maxNorth = 180.0;

		for (Vertex v : this.getCoordinates()) {
			minWest = Math.min(minWest, v.getLatitudeDegress());
			maxNorth = Math.max(maxNorth, v.getLongitudeDegress());
		}
		return new Vertex(maxNorth, minWest);
	}

	@JsonIgnore
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
