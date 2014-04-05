package me.math.grid;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import me.math.Vertex;
import me.math.grid.array.SpatialGridPoint;

@XStreamAlias("SpatialGridData")
public abstract class SpatialGridData {

	private SpatialGridPoint gridPointReferenece = null;
	
	@XStreamAlias("interpolationValue")
	private double interpolationValue = 0.0;

	protected SpatialGridData() {
	}

	/**
	 * @return the gridPointReferenece
	 */
	public SpatialGridPoint getGridPointReferenece() {
		return gridPointReferenece;
	}

	/**
	 * @return the gridPointReferenece
	 */
	public void setGridPointReferenece(SpatialGridPoint reference) {
		this.gridPointReferenece = reference;
	}

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
	 * The latitude in Degress of Point.
	 * @return
	 */
	public double getLatitudeDegress()
	{
		return this.getGridPointReferenece().getVertex().getLatitudeDegress();
	}

	/**
	 * The longitude in Degress of Point.
	 * @return
	 */
	public double getLongitudeDegress()
	{
		return this.getGridPointReferenece().getVertex().getLongitudeDegress();
	}

	/**
	 * 
	 * @return
	 */
	public Vertex toVertex() {
		return this.getGridPointReferenece().getVertex();
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract double getValue();

}
