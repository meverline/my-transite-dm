package org.transiteRepositry.server.request.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import me.utils.TransiteEnums;

@XStreamAlias("Distance")
public class Distance {

	@XStreamAlias("unit")
	@XStreamAsAttribute
	private String unit;
	@XStreamAlias("length")
	private double value;
	
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * return distance in meters.
	 * @return
	 */
	public double toMeters()
	{
		TransiteEnums.DistanceUnitType type = TransiteEnums.DistanceUnitType.valueOf(getUnit().toUpperCase());
		
		if ( type == null ) {
			throw new IllegalArgumentException("invlide distance unit: " + getUnit());
		}
		return type.toMeters(getValue());
	}
	
}
