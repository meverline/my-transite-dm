package org.transiteRepositry.server.request.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

@XStreamAlias("LatLonBox")
public class LatLonBox {

	@XStreamOmitField
	private static GeometryFactory factory = new GeometryFactory();
	
	@XStreamAlias("east")
	private double east;
	@XStreamAlias("west")
	private double west;
	@XStreamAlias("north")
	private double north;
	@XStreamAlias("south")
	private double south;
	
	/**
	 * @return the east
	 */
	public double getEast() {
		return east;
	}
	/**
	 * @param east the east to set
	 */
	public void setEast(double east) {
		this.east = east;
	}
	/**
	 * @return the west
	 */
	public double getWest() {
		return west;
	}
	/**
	 * @param west the west to set
	 */
	public void setWest(double west) {
		this.west = west;
	}
	/**
	 * @return the north
	 */
	public double getNorth() {
		return north;
	}
	/**
	 * @param north the north to set
	 */
	public void setNorth(double north) {
		this.north = north;
	}
	/**
	 * @return the south
	 */
	public double getSouth() {
		return south;
	}
	/**
	 * @param south the south to set
	 */
	public void setSouth(double south) {
		this.south = south;
	}
	
	/**
	 * Return the upper right corner of Box.
	 * @return
	 */
	public Point getUpperRight()
	{
		return factory.createPoint( new Coordinate(getWest(), getNorth()));
	}
	
	/**
	 * Return the upper right corner of Box.
	 * @return
	 */
	public Point getUpperLeft()
	{
		return factory.createPoint( new Coordinate(getEast(), getNorth()));
	}
	
	/**
	 * Return the lower left corner of Box.
	 * @return
	 */
	public Point getLowerLeft()
	{
		return factory.createPoint( new Coordinate(getEast(), getSouth()));
	}
	
	/**
	 * Return the lower left corner of Box.
	 * @return
	 */
	public Point getLowerRight()
	{
		return factory.createPoint( new Coordinate(getWest(), getSouth()));
	}
}
