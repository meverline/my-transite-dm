package me.datamining.shapes;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import me.transit.dao.query.SpatialQuery;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;

@Jacksonized
@Data
public class Rectanlge implements Shape{

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

	@Override
	public void setQueryShape(SpatialQuery query) {
		query.addRectangleConstraint(this.getUpperLeft().toPoint(),
									 this.getLowerRight().toPoint());
	}
}
