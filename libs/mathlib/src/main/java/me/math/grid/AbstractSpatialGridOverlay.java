package me.math.grid;

import me.math.Vertex;
import me.math.kdtree.KDTree;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class AbstractSpatialGridOverlay extends AbstractSpatialGrid {

	public static final String CROSSSCOVARIANCE = "crossCovariance";

	@Column(name="gridSpacingMeters" )
	private double gridSpacingMeters_ = 1000;
	
	@Column(name="upperLeft", columnDefinition = "Geometry")
	private Vertex upperLeft_ = null;
	
	@Column(name="lowerRight", columnDefinition = "Geometry")
	private Vertex lowerRight_ = null;
	
	@Column(name="crossCovariance" )
	private double crossCovariance_ = 0;

	/**
	 * 
	 * @param spacingInMeters
	 */
	protected void init(double spacingInMeters)
	{
		setGridSpacingMeters(spacingInMeters);
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("gridSpacingMeters")
	public double getGridSpacingMeters() {
		return gridSpacingMeters_;
	}
	
	/**
	 * 
	 * @param gridSpacingMeters_
	 */
	@JsonSetter("gridSpacingMeters")
	public void setGridSpacingMeters(double gridSpacingMeters_) {
		this.gridSpacingMeters_ = gridSpacingMeters_;
	}
	
	/**
	 * @return the crossCovariance
	 */
	@JsonGetter("crossCovariance")
	public final double getCrossCovariance() {
		return crossCovariance_;
	}

	/**
	 * @param crossCovariance the crossCovariance to set
	 */
	@JsonSetter("crossCovariance")
	protected void setCrossCovariance(double crossCovariance) {
		this.crossCovariance_ = crossCovariance;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("lowerRight")
	public Vertex getLowerRight() {
		return lowerRight_;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("upperLeft")
	public Vertex getUpperLeft() {
		return upperLeft_;
	}
	
	/** 
	 * @param upperLeft_
	 */
	@JsonSetter("upperLeft")
	public void setUpperLeft(Vertex upperLeft_) {
		this.upperLeft_ = upperLeft_;
	}

	/**
	 * 
	 * @param lowerRight_
	 */
	@JsonSetter("lowerRight")
	public void setLowerRight(Vertex lowerRight_) {
		this.lowerRight_ = lowerRight_;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMaxLatitude()
	{
		return lowerRight_.getLatitudeDegress();
	}

	/**
	 * 
	 * @return
	 */
	public double getMaxLongitude()
	{
		return lowerRight_.getLongitudeDegress();
	}
	
	/**
	 * 
	 * @param upperLeft
	 * @param lowerRight
	 * @return
	 */
	protected Vertex findAverageLatLon(Vertex upperLeft, Vertex lowerRight)
	{
		double avg_lat = (upperLeft.getLatitudeDegress() + lowerRight.getLatitudeDegress()) / 2.0;
		double avg_lon = (upperLeft.getLongitudeDegress() + lowerRight.getLongitudeDegress()) / 2.0;
		
		return new Vertex( avg_lat, avg_lon);
	}
	
	public abstract AbstractSpatialGridPoint get(int index, int gridIndex);
	
	/**
	 * 
	 * @return
	 */
	public abstract KDTree getTree();

}
