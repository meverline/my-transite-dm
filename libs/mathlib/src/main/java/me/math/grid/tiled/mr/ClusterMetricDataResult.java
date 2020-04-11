package me.math.grid.tiled.mr;

import java.util.ArrayList;
import java.util.List;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;

public class ClusterMetricDataResult extends MetricDataResult {
	
	private List<Long> values_ = new ArrayList<>();

	public ClusterMetricDataResult(Vertex aPoint) {
		super(aPoint);
	}
	
	/**
	 * @return the value_
	 */
	public List<Long> getValue() {
		return values_;
	}
	
	/**
	 * @param value the value_ to set
	 */
	public void setValue(long value) {
		this.values_.add(value);
	}
	
	@Override
	public void addDataToGridPoint(AbstractSpatialGridPoint pt) {
	}

}
