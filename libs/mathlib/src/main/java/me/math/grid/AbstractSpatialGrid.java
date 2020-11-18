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

package me.math.grid;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import me.math.EarthConstants;
import me.math.Vertex;
import me.math.grid.data.CrossCovData;
import me.math.kdtree.KDTree;

public abstract class AbstractSpatialGrid implements Cloneable {

	private int rows_ = 0;
	private int cols_ = 0;
	private double gridSpacingMeters_ = 1000;
	private CrossCovData ccdata;

	protected AbstractSpatialGrid()
	{
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonGetter("cols")
	@DynamoDBAttribute(attributeName = "cols")
	public int getCols() {
		return cols_;
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
	 * 
	 * @return
	 */
	@JsonGetter("rows")
	@DynamoDBAttribute(attributeName = "rows")
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
	 * @param upperLeft
	 * @param lowerRight
	 * @param spacingInMeters
	 * @return
	 */
	protected static int findNumberOfRows(final Vertex upperLeft, final Vertex lowerRight, final double spacingInMeters) {
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
	protected static int findNumberOfCols(final Vertex upperLeft, final Vertex lowerRight, final double spacingInMeters) {
		Vertex lowerLeft = new Vertex(lowerRight.getLatitudeDegress(),
									  upperLeft.getLongitudeDegress());

		double lonGroundRangeMeters = EarthConstants.distanceMeters(lowerLeft, lowerRight);		
		return (int) Math.floor(lonGroundRangeMeters / spacingInMeters) + 1;
	}
	
	public abstract SpatialGridPoint getNextGridPoint(SpatialGridPoint gridPt);

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
	@DynamoDBAttribute(attributeName = "gridSpacingMeters")
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
	@JsonIgnore
	public final double getCrossCovariance() {
		return this.getCcdata().crossCovariance();
	}

	/**
	 * @return the ccdata
	 */
	@JsonGetter("cc_data")
	@DynamoDBTypeConverted(converter = CrossCovData.DynamoConvert.class)
	@DynamoDBAttribute(attributeName = "cc_data")
	public CrossCovData getCcdata() {
		return ccdata;
	}

	/**
	 * @param ccdata the ccdata to set
	 */
	@JsonSetter("cc_data")
	public void setCcdata(CrossCovData ccdata) {
		this.ccdata = ccdata;
	}

	/**
	 *
	 * @param upperLeft
	 * @param lowerRight
	 * @return
	 */
	protected Vertex findAverageLatLon(final Vertex upperLeft, final Vertex lowerRight)
	{
		double avg_lat = (upperLeft.getLatitudeDegress() + lowerRight.getLatitudeDegress()) / 2.0;
		double avg_lon = (upperLeft.getLongitudeDegress() + lowerRight.getLongitudeDegress()) / 2.0;

		return new Vertex( avg_lat, avg_lon);
	}

	public abstract SpatialGridPoint get(int index, int gridIndex);

	/**
	 *
	 * @return
	 */
	public abstract KDTree getTree();

}
