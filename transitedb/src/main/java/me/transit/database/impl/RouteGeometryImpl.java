package me.transit.database.impl;

import me.transit.database.RouteGeometry;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Geometry;

public class RouteGeometryImpl extends TransitDateImpl implements RouteGeometry {

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
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
	
	/**
	 * 
	 */
	public boolean valid() 
	{
		return true;
	}
	
}
