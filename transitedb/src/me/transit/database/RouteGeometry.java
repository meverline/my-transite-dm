package me.transit.database;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.vividsolutions.jts.geom.Geometry;

@XStreamAlias("RouteGeometry")
public interface RouteGeometry extends TransitData {
	
	/**
	 * @return the shape
	 */
	public Geometry getShape();

	/**
	 * @param shape the shape to set
	 */
	public void setShape(Geometry shape);
	
}
