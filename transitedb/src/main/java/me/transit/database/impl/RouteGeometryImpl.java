package me.transit.database.impl;

import me.transit.database.RouteGeometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name="tran_route_geometry")
public class RouteGeometryImpl extends TransitDateImpl implements RouteGeometry {

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("shape")
	@XStreamConverter(me.database.LineStringConverter.class)
	@Column(name="SHAPE", columnDefinition = "Geometry")
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
