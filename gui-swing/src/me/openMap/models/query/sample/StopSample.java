package me.openMap.models.query.sample;

import me.datamining.sample.AbstractSpatialSampleData;
import me.datamining.sample.TransitStopSpatialSample;

public class StopSample extends DataSample {

	/*
	 * (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getName()
	 */
	@Override
	public String toString() {
		return "Route Serving Stop";
	}

	/*
	 * (non-Javadoc)
	 * @see me.openMap.models.query.sample.DataSample#getSampleType()
	 */
	@Override
	public AbstractSpatialSampleData getSampleType() {
		return new TransitStopSpatialSample();
	}
	
}
