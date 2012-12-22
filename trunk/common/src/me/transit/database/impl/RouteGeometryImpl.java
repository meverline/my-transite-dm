package me.transit.database.impl;

import me.transit.database.RouteGeometry;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.vividsolutions.jts.geom.Geometry;

public class RouteGeometryImpl extends TransitDateImpl implements RouteGeometry {

	@XStreamAlias("shape")
	@XStreamConverter(me.database.LineStringConverter.class)
	private Geometry shape = null;

	/**
	 * @return the shape
	 */
	public Geometry getShape() {
		return shape;
	}

	/**
	 * @param shape the shape to set
	 */
	public void setShape(Geometry shape) {
		this.shape = shape;
	}
	
}
