package me.math.grid.tiled.mr;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;

@XStreamAlias("MetricDataResult")
public abstract class MetricDataResult {

	@XStreamAlias("Location")
	private Vertex location_;
	
	public abstract void addDataToGridPoint(AbstractSpatialGridPoint pt);
	
	protected MetricDataResult(Vertex aPoint) {
		this.setLocatoin(aPoint);
	}

	/**
	 * @return the center_
	 */
	public Vertex getLocation() {
		return location_;
	}

	/**
	 * @param center_ the center_ to set
	 */
	protected void setLocatoin(Vertex center) {
		this.location_ = center;
	}
		
}
