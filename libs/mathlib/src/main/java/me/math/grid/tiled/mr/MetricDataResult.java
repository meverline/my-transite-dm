package me.math.grid.tiled.mr;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;
import me.math.grid.SpatialGridPoint;

public abstract class MetricDataResult {

	private Vertex location_;
	
	public abstract void addDataToGridPoint(SpatialGridPoint pt);
	
	protected MetricDataResult(Vertex aPoint) {
		this.setLocatoin(aPoint);
	}

	/**
	 * @return the center_
	 */
	@JsonGetter("location")
	public Vertex getLocation() {
		return location_;
	}

	/**
	 * @param center the center_ to set
	 */
	@JsonSetter("location")
	protected void setLocatoin(Vertex center) {
		this.location_ = center;
	}
		
}
