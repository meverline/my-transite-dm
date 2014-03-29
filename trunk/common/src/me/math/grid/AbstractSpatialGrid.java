package me.math.grid;

//	  CIRAS: Crime Information Retrieval and Analysis System
//Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//Based Upon C# code by Frank Levine and Paul Greene
//Adapted to Java by Mark Everline

import com.thoughtworks.xstream.annotations.XStreamAlias;

import me.math.EarthConstants;
import me.math.Vertex;

public abstract class AbstractSpatialGrid {

	@XStreamAlias("SpacingMeters")
	private double gridSpacingMeters_ = 1000;
	@XStreamAlias("Row")
	private int rows_ = 0;
	@XStreamAlias("Cols")
	private int cols_ = 0;
	@XStreamAlias("ULV")
	private Vertex upperLeft_ = null;
	@XStreamAlias("LRV")
	private Vertex lowerRight_ = null;
		
	protected AbstractSpatialGrid()
	{
	}
	
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
	public int getCols() {
		return cols_;
	}

	/**
	 * 
	 * @return
	 */
	public int getRows() {
		return rows_;
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
	protected void setGridSpacingMeters(double gridSpacingMeters_) {
		this.gridSpacingMeters_ = gridSpacingMeters_;
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
	 * 
	 * @param rows_
	 */
	protected void setRows(int rows_) {
		this.rows_ = rows_;
	}

	/**
	 * 
	 * @param cols_
	 */
	protected void setCols(int cols_) {
		this.cols_ = cols_;
	}

	/**
	 * 
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
	 * @param spacingInMeters
	 * @return
	 */
	public static int findNumberOfRows(Vertex upperLeft, Vertex lowerRight, double spacingInMeters) {
		Vertex upperRight = new Vertex(upperLeft.getLatitudeDegress(),
				lowerRight.getLongitudeDegress());

		double latGroundRangeMeters = EarthConstants.distanceMeters(upperRight,lowerRight);
		return (int) Math.floor((latGroundRangeMeters / spacingInMeters) + 0.5f) + 1;
	}
	
	/**
	 * 
	 * @param upperLeft
	 * @param lowerRight
	 * @param spacingInMeters
	 * @return
	 */
	public static int findNumberOfCols(Vertex upperLeft, Vertex lowerRight, double spacingInMeters) {
		Vertex lowerLeft = new Vertex(lowerRight.getLatitudeDegress(),
									  upperLeft.getLongitudeDegress());

		double lonGroundRangeMeters = EarthConstants.distanceMeters(lowerLeft, lowerRight);		
		return (int) Math.floor(lonGroundRangeMeters / spacingInMeters) + 1;
	}
	
	public abstract AbstractSpatialGridPoint getNextGridPoint(AbstractSpatialGridPoint gridPt);
}
