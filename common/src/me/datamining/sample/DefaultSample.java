/**
 * 
 */
package me.datamining.sample;

import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author meverline
 *
 */
@XStreamAlias("SpatialData")
public class DefaultSample extends AbstractSpatialSampleData {
	
	protected double getMetric(TransitStop stop)
	{
		return 0;
	}
	
	public double getValue() {
		return this.getSampleValue();
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getSampleValue()
	 */
	@Override
	public double getSampleValue() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getMin()
	 */
	@Override
	public double getMin() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getMax()
	 */
	@Override
	public double getMax() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#average()
	 */
	@Override
	public double average() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#standardDeviation()
	 */
	@Override
	public double standardDeviation() {
		return 0;
	}

}
