package me.datamining;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;

@JsonRootName("DataItem")
public class DataItem {
	
	private Vertex location = null;
	private double value = 0.0;
	
	public DataItem() {
	}

	public DataItem(Vertex location, double value) {
		this.setLocation(location);
		this.setValue(value);
	}
	
	/**
	 * @return the location
	 */
	@JsonGetter("location")
	public Vertex getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	@JsonSetter("location")
	public void setLocation(Vertex location) {
		this.location = location;
	}

	/**
	 * @return the value
	 */
	@JsonGetter("value")
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	@JsonGetter("value")
	public void setValue(double value) {
		this.value = value;
	}

}
