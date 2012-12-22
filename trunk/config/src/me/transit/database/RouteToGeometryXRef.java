package me.transit.database;

public interface RouteToGeometryXRef {
	
	/**
	 * @return the uuid
	 */
	public long getUUID();
	/**
	 * @param uuid the uuid to set
	 */
	public void setUUID(long uuid);
	/**
	 * @return the geometry
	 */
	public RouteGeometry getShape();
	/**
	 * @param geometry the geometry to set
	 */
	public void setShape(RouteGeometry geometry);
	
}
