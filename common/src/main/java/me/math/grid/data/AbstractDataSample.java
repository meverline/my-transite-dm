package me.math.grid.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
//@JsonSubTypes({ @Type(value = DensityEstimateDataSample.class, name = "DensityEstimateDataSample"),
//		@Type(value = STINGDataSample.class, name = "STINGDataSample") })
public abstract class AbstractDataSample {

	private double interpolationValue = 0.0;

	/**
	 * 
	 * @param value
	 */
	@JsonSetter("interpolationValue")
	public void setInterpolationValue(double value) {
		this.interpolationValue = value;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("interpolationValue")
	public double getInterpolationValue() {
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
