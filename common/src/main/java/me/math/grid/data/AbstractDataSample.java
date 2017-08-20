package me.math.grid.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public abstract class AbstractDataSample {

	@XStreamAlias("interpolationValue")
	private double interpolationValue = 0.0;
	
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
	
	/**
	 * 
	 */
	public abstract void addValue(double value);
	
}
