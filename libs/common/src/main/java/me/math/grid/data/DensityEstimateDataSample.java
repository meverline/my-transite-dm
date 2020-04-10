/**
 * 
 */
package me.math.grid.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * @author markeverline
 *
 */
@JsonRootName(value = "DensityEstimateDataSample")
public class DensityEstimateDataSample extends AbstractDataSample {

	private double value = 0;
	
	/* (non-Javadoc)
	 * @see me.math.grid.data.AbstractDataSample#getValue()
	 */
	@Override
	@JsonGetter("value")
	public double getValue() {
		return this.value;
	}

	@Override
	@JsonSetter("value")
	public void addValue(double value) {
		this.value += value;
	}

}
