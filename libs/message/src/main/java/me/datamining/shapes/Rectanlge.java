package me.datamining.shapes;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;

@JsonRootName("Rectanlge")
public class Rectanlge implements Shape{
	
	private final GeometryFactory factory_  = new GeometryFactory();
	private Vertex upperLeft;
	private Vertex lowerRight;

	public Rectanlge() {
	}

	/**
	 * 
	 * @param upperLeft
	 * @param lowerRight
	 */
	public Rectanlge(Vertex upperLeft, Vertex lowerRight) {
		this.upperLeft = upperLeft;
		this.lowerRight = lowerRight;	
	}

	/**
	 * @return the upperLeft
	 */
	@JsonGetter("upperLeft")
	public Vertex getUpperLeft() {
		return upperLeft;
	}

	/**
	 * @param upperLeft the upperLeft to set
	 */
	@JsonSetter("upperLeft")
	public void setUpperLeft(Vertex upperLeft) {
		this.upperLeft = upperLeft;
	}

	/**
	 * @return the lowerRight
	 */
	@JsonGetter("lowerRight")
	public Vertex getLowerRight() {
		return lowerRight;
	}

	/**
	 * @param lowerRight the lowerRight to set
	 */
	@JsonSetter("lowerRight")
	public void setLowerRight(Vertex lowerRight) {
		this.lowerRight = lowerRight;
	}
	
	/*
	 * 
	 */
	@Override
	public Geometry shape() {

		List<Coordinate> coords = new ArrayList<Coordinate>();
		
		Point ul = getUpperLeft().toPoint();
		Point lr = getLowerRight().toPoint();
		
		coords.add(ul.getCoordinate());
		coords.add(new Coordinate( lr.getCoordinate().x, ul.getCoordinate().y));
		coords.add(lr.getCoordinate());
		coords.add(new Coordinate( ul.getCoordinate().x, lr.getCoordinate().y));
		coords.add(coords.get(0));
		
		Coordinate array[] = new Coordinate[coords.size()];
		coords.toArray(array);

        return factory_.createPolygon(factory_.createLinearRing(array), null);
	}
	
}
