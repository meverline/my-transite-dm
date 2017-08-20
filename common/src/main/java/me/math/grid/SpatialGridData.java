package me.math.grid;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SpatialGridData")
public abstract class SpatialGridData {

	private long gridPointReferenece = -1;
	
	@XStreamAlias("interpolationValue")
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
	public void setInterpolationValue(double value)
	{
		this.interpolationValue = value;
	}

	/**
	 * 
	 * @return
	 */
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
