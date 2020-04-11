package me.math.grid;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

//	  CIRAS: Crime Information Retrieval and Analysis System
//Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
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

import me.math.EarthConstants;
import me.math.Vertex;

public abstract class AbstractSpatialGrid {
	
	public static final String ROWS = "rows";
	public static final String COLS = "cols";

	private int rows_ = 0;
	private int cols_ = 0;
		
	protected AbstractSpatialGrid()
	{
	}
	
	/**
	 * 
	 * @return
	 */
	@Column(name="numColumns")
	@JsonGetter("cols")
	public int getCols() {
		return cols_;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name="numRows")
	@JsonGetter("rows")
	public int getRows() {
		return rows_;
	}

	/**
	 * 
	 * @param rows_
	 */
	@JsonSetter("rows")
	protected void setRows(int rows_) {
		this.rows_ = rows_;
	}

	/**
	 * 
	 * @param cols_
	 */
	@JsonSetter("cols")
	protected void setCols(int cols_) {
		this.cols_ = cols_;
	}

	/**

	
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