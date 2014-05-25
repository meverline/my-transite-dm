package me.datamining.mapreduce;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import me.math.Vertex;

@XStreamAlias("DataResult")
public class DataResult {

	@XStreamAlias("point")
	private Vertex point = null;
	@XStreamAlias("metric")
	private double metric = 0;
	
	/**
	 * Constructor
	 */
	public DataResult()
	{
	}
	
	/**
	 * 
	 * @param corner
	 * @param value
	 */
	public DataResult(Vertex corner, double value)
	{
		point = corner;
		metric = value;
	}
	
	/**
	 * @return the point
	 */
	public Vertex getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(Vertex point) {
		this.point = point;
	}

	/**
	 * @return the metric
	 */
	public double getMetric() {
		return metric;
	}

	/**
	 * @param metric the metric to set
	 */
	public void setMetric(double metric) {
		this.metric = metric;
	}
	
	public void reset()
	{
		this.metric = 0;
		this.point = null;
	}

}
