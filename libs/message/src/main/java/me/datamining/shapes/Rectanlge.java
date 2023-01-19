package me.datamining.shapes;

import lombok.Data;
import me.math.Vertex;
import me.transit.dao.query.SpatialQuery;

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
