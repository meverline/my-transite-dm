package me.math.grid;

import me.math.Vertex;
import me.math.kdtree.KDTree;

import javax.persistence.Column;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public abstract class AbstractSpatialGridOverlay extends AbstractSpatialGrid {

	public static final String CROSSSCOVARIANCE = "crossCovariance";

	@Column(name="gridSpacingMeters" )
	@XStreamAlias("SpacingMeters")
	private double gridSpacingMeters_ = 1000;
	
	@Column(name="upperLeft", columnDefinition = "Geometry")
	@XStreamAlias("ULV")
	private Vertex upperLeft_ = null;
	
	@Column(name="lowerRight", columnDefinition = "Geometry")
	@XStreamAlias("LRV")
	private Vertex lowerRight_ = null;
	
	@Column(name="crossCovariance" )
	@XStreamAlias(AbstractSpatialGridOverlay.CROSSSCOVARIANCE)
	private double crossCovariance_ = 0;

	/**
	 * 
	 * @param spacing
	 */
	protected void init(double spacingInMeters)
	{
		setGridSpacingMeters(spacingInMeters);
	}

	/**
	 * 
	 * @return
	 */
	public double getGridSpacingMeters() {
		return gridSpacingMeters_;
	}
	
	/**
	 * 
	 * @param gridSpacingMeters_
	 */
	public void setGridSpacingMeters(double gridSpacingMeters_) {
		this.gridSpacingMeters_ = gridSpacingMeters_;
	}
	
	/**
	 * @return the crossCovariance
	 */
	public final double getCrossCovariance() {
		return crossCovariance_;
	}

	/**
	 * @param crossCovariance the crossCovariance to set
	 */
	protected void setCrossCovariance(double crossCovariance) {
		this.crossCovariance_ = crossCovariance;
	}

	/**
	 * 
	 * @return
	 */
	public Vertex getLowerRight() {
		return lowerRight_;
	}

	/**
	 * 
	 * @return
	 */
	public Vertex getUpperLeft() {
		return upperLeft_;
	}
	
	/** 
	 * @param upperLeft_
	 */
	protected void setUpperLeft(Vertex upperLeft_) {
		this.upperLeft_ = upperLeft_;
	}

	/**
	 * 
	 * @param lowerRight_
	 */
	protected void setLowerRight(Vertex lowerRight_) {
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
