package me.math.grid;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class SpatialGridData {

	private long gridPointReferenece = -1;
	private double interpolationValue = 0.0;
	
	protected SpatialGridData() 
	{
	}

	protected SpatialGridData(long gridPointIndex) 
	{
		gridPointReferenece = gridPointIndex;
	}

	/**
	 * @return the gridPointReferenece
	 */
	public long getGridPointIndex() {
		return gridPointReferenece;
	}

	/**
	 * @return the gridPointReferenece
	 */
	public void setGridPointIndex(long reference) {
		this.gridPointReferenece = reference;
	}

	/**
	 * 
	 * @param value
	 */
	@JsonSetter("interpolationValue")
	public void setInterpolationValue(double value)
	{
		this.interpolationValue = value;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("interpolationValue")
	public double getInterpolationValue()
	{
		return this.interpolationValue;
	}
		
	/**
	 * 
	 * @return
	 */
	public abstract double getValue();

}
