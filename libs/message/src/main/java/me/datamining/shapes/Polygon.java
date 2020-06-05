package me.datamining.shapes;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;

@JsonRootName("Polygon")
public class Polygon implements Shape{
	
	private final GeometryFactory factory_  = new GeometryFactory();
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

	/**
	 * @return the coordinates
	 */
	@JsonGetter("coordinates")
	public List<Vertex> getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	@JsonSetter("coordinates")
	public void setCoordinates(List<Vertex> coordinates) {
		this.coordinates = coordinates;
	}
	
	/*
	 * 
	 */
	@Override
	public Geometry shape()  {

		List<Coordinate> coords = new ArrayList<Coordinate>();
		
		for ( Vertex pt : getCoordinates() ) {
			coords.add(pt.toCoordinate());
		}
		coords.add(coords.get(0));
		
		Coordinate array[] = new Coordinate[coords.size()];
		coords.toArray(array);

        return factory_.createPolygon(factory_.createLinearRing(array), null);
	}


}
