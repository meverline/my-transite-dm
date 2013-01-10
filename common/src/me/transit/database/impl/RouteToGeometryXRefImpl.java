package me.transit.database.impl;

import me.transit.database.RouteGeometry;
import me.transit.database.RouteToGeometryXRef;

public class RouteToGeometryXRefImpl implements RouteToGeometryXRef {

	private static final long serialVersionUID = 1L;
	private long uuid = -1;
	private RouteGeometry shape = null;
	
	/**
	 * 
	 */
	public RouteToGeometryXRefImpl() {}
	
	/**
	 * 
	 * @param shape
	 */
	public RouteToGeometryXRefImpl(RouteGeometry shape) {
		this.shape = shape;
	}
	
	/**
	 * @return the uuid
	 */
	public long getUUID() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return the geometry
	 */
	public RouteGeometry getShape() {
		return shape;
	}
	/**
	 * @param geometry the geometry to set
	 */
	public void setShape(RouteGeometry geometry) {
		this.shape = geometry;
	}
}
