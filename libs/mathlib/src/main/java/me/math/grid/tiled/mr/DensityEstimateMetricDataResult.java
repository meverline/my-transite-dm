package me.math.grid.tiled.mr;

import me.math.Vertex;
import me.math.grid.SpatialGridPoint;

public class DensityEstimateMetricDataResult extends MetricDataResult {
	
	private long value_ = 0;
	
	
	public DensityEstimateMetricDataResult(Vertex aPoint) {
		super(aPoint);
	}

	/**
	 * @return the value_
	 */
	public long getValue() {
		return value_;
	}
	
	/**
	 * @param value the value_ to set
	 */
	public void setValue(long value) {
		this.value_ = value;
	}

	@Override
	public void addDataToGridPoint(SpatialGridPoint pt) {
		
	}
	
}
