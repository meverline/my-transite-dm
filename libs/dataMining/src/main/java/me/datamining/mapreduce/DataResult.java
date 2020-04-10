package me.datamining.mapreduce;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;


public class DataResult {


	private Vertex point = null;
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
	
	
	public DataResult(DataResult copy)
	{
		point = copy.getPoint();
		metric = copy.getMetric();
	}
	
	/**
	 * @return the point
	 */
	@JsonGetter("point")
	public Vertex getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	@JsonSetter("point")
	public void setPoint(Vertex point) {
		this.point = point;
	}

	/**
	 * @return the metric
	 */
	@JsonGetter("metric")
	public double getMetric() {
		return metric;
	}

	/**
	 * @param metric the metric to set
	 */
	@JsonSetter("metric")
	public void setMetric(double metric) {
		this.metric = metric;
	}
	
	public void reset()
	{
		this.metric = 0;
		this.point = null;
	}

}
