/**
 * 
 */
package me.math.grid.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author markeverline
 *
 */

@XStreamAlias("DensityEstimateDataSample")
public class DensityEstimateDataSample extends AbstractDataSample {

	@XStreamAlias("Value")
	private double value;
	
	/* (non-Javadoc)
	 * @see me.math.grid.data.AbstractDataSample#getValue()
	 */
	@Override
	public double getValue() {
		return this.value;
	}

	@Override
	public void addValue(double value) {
		this.value += value;
	}

}
