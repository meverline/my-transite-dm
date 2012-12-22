//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.math.grid;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.math.EarthConstants;
import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;

import com.vividsolutions.jts.geom.Point;

public class UniformSpatialGrid {
	
	protected SpatialGridPoint[][] grid_ = null;
	private double gridSpacingMeters_ = 1000;
	private int rows_ = 0;
	private int cols_ = 0;
	private Vertex upperLeft_ = null;
	private Vertex lowerRight_ = null;
	
	private Log logger = LogFactory.getLog(UniformSpatialGrid.class);
	
	/**
	 * 
	 * @param spacing
	 */
	public UniformSpatialGrid( double spacingInMeters)
	{
		init(spacingInMeters);
	}

	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacing
	 */
	public UniformSpatialGrid(Point ul, Point lr, double spacingInMeters) {
		init(spacingInMeters);
		setUpperLeft( new Vertex(ul.getX(), ul.getY()));
		setLowerRight( new Vertex(lr.getX(), lr.getY()));

		createGrid(getUpperLeft(), getLowerRight());
	}
	
	/**
	 *
	 * @param ul
	 * @param lr
	 * @param spacing
	 */
	public UniformSpatialGrid(Vertex ul, Vertex lr, double spacingInMeters) {
		init(spacingInMeters);
		setUpperLeft( ul);
		setLowerRight( lr );

		createGrid(getUpperLeft(), getLowerRight());
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
	 * @param gridSpacingMeters_
	 */
	protected void setGridSpacingMeters(double gridSpacingMeters_) {
		this.gridSpacingMeters_ = gridSpacingMeters_;
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
	 * @param file
	 * @throws FileNotFoundException
	 */
	public void toCSVFile(String file) throws FileNotFoundException
	{
		PrintStream ps = new PrintStream(new FileOutputStream(file));

		for ( int  r = 0; r < getRows(); r++) {
			for ( int c = 0; c< getCols(); c++) {
				if ( c != 0 ) { ps.print(","); }
				ps.print( get(r,c).toString());
			}
			ps.println();
		}
		ps.close();
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
	 * @param gridPt
	 * @return
	 */
	public SpatialGridPoint getNextGridPoint(SpatialGridPoint gridPt)
	{
		SpatialGridPoint next = null;

		if (gridPt.getRow() == 0 ) {
			next = get(gridPt.getRow()+1, gridPt.getCol());
		} else if ( gridPt.getRow()+1 >= getRows() ) {
			next = get(gridPt.getRow()-1, gridPt.getCol());
		} else if ( gridPt.getCol() == 0) {
			next = get(gridPt.getRow()-1, gridPt.getCol());
		} else if ( gridPt.getCol()+1 >= getCols() ) {
			next = get(gridPt.getRow()-1, gridPt.getCol());
		} else {
			next = get(gridPt.getRow(), gridPt.getCol()+1);
		}
		return next;
	}

	/**
	 * Binary search to find the longitude.
	 * @param location
	 * @param startLoc
	 * @param endLoc
	 * @param row
	 * @return
	 */
	protected int findLongitude(Vertex location, int startLoc, int endLoc, int row)
	{
		double size = Math.abs(startLoc - endLoc);
		int midpoint = startLoc + (int) Math.floor((size / 2.0) + 0.5f);

		try {

			SpatialGridPoint pt = this.get(row, midpoint);
			SpatialGridPoint lower = this.get(row, midpoint + 1);

			if (pt == null || lower == null  ) {
				return -1;
			}
			if (location.getLongitudeDegress() >= pt.getVertex().getLongitudeDegress()
					&& location.getLongitudeDegress() <= lower.getVertex().getLongitudeDegress()) {
				return midpoint;
			} else {
				if (location.getLongitudeDegress() < pt.getVertex().getLongitudeDegress()) {
					midpoint = findLongitude(location, startLoc, midpoint - 1,row);
				} else {
					midpoint = findLongitude(location, midpoint + 1, endLoc,row);
				}
			}
		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage() + " " + midpoint + " " + startLoc + " " + endLoc;
			throw new ArrayIndexOutOfBoundsException(msg);
		}
		return midpoint;
	}

	/**
	 * Binary search for the GridPoint that the location is in.
	 * @param location
	 * @param startLoc
	 * @param endLoc
	 * @return
	 */
	protected int findLatitude(Vertex location, int startLoc, int endLoc)
	{
		double size = Math.abs(startLoc-endLoc);
		int midpoint = startLoc + (int) Math.floor((size / 2.0) + 0.5f);
		
		try {

			SpatialGridPoint pt = this.get(midpoint, 0);
			SpatialGridPoint lower = this.get(midpoint + 1, 0);

			if (pt == null || lower == null ) {
				return -1;
			}
			if (location.getLatitudeDegress() >= pt.getVertex().getLatitudeDegress()
					&& location.getLatitudeDegress() <= lower.getVertex().getLatitudeDegress()) {
				return midpoint;
			} else {
				if (location.getLatitudeDegress() < pt.getVertex().getLatitudeDegress()) {
					midpoint = findLatitude(location, startLoc, midpoint - 1);
				} else {
					midpoint = findLatitude(location, midpoint + 1, endLoc);
				}
			}
			
		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage() +" " + midpoint + " " + startLoc + " " + endLoc;
			throw new ArrayIndexOutOfBoundsException(msg);
		}

	    return midpoint;
	}
	
	/**
	 * 
	 * @param apoint
	 * @return
	 */
    public SpatialGridPoint findGridPont(Point apoint) {
        Vertex location = new Vertex(apoint.getY(), apoint.getX());
        
        int row = this.findLatitude(location, 0, this.getRows());
        if (row == -1) {
        	//logger.info("findGridPont row: " + row );
            return null;
        }
        int col = this.findLongitude(location, 0, this.getCols(), row);
        if (col == -1) {
        	//logger.info("findGridPont col: " + col );
            return null;
        }
        
        //logger.info("findGridPont row: " + row + " col: " + col);
        return this.get(row, col);
    }


	/**
	 * 
	 * @param row
	 * @param col
	 */
	protected void createGrid(int row, int col)
	{
		grid_ = new SpatialGridPoint[row][col];
	}

	/**
	 * 
	 * @param row
	 * @param col
	 */
	protected void initGridPoit( int row, int col)
	{
		grid_[row][col] = null;
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param point
	 * @param index
	 */
	protected void addGridPoint( int row, int col, Vertex point, int index)
	{
		grid_[row][col] = new SpatialGridPoint(row, col, point, index, this);
	}


	/**
	 * Create a uniform lat/lon grid.
	 * @param upperLeft
	 * @param lowerRight
	 */
	protected void createGrid(Vertex upperLeft, Vertex lowerRight) {
		Vertex upperRight = new Vertex(upperLeft.getLatitudeDegress(),
									   lowerRight.getLongitudeDegress());

		Vertex lowerLeft = new Vertex(lowerRight.getLatitudeDegress(),
									  upperLeft.getLongitudeDegress());

		double lonGroundRangeMeters = EarthConstants.distanceMeters(lowerLeft, lowerRight);
		double latGroundRangeMeters = EarthConstants.distanceMeters(upperRight, lowerRight);

		cols_ = (int) Math.floor((lonGroundRangeMeters / gridSpacingMeters_) + 0.5f) + 1;

		rows_ = (int) Math.floor((latGroundRangeMeters / gridSpacingMeters_) + 0.5f) + 1;

		createGrid(rows_, cols_);
		
		logger.info("UniformSpatialGrid Rows: " + rows_ + " Cols: " + cols_);

		LocalDownFrame southWestFrame = new LocalDownFrame(lowerLeft.getEcfFromLatLon());

		for (int rowIndex = 0; rowIndex < rows_; rowIndex++) {
			for (int colIndex = 0; colIndex < cols_; colIndex++) {
				initGridPoit(rowIndex, colIndex);
			}
		}

		int number = 0;
		for (int rowIndex = 0; rowIndex < rows_; rowIndex++) {
			for (int colIndex = 0; colIndex < cols_; colIndex++) {
				double northDistanceMeters = (double) rowIndex* gridSpacingMeters_;
				double eastDistanceMeters = (double) colIndex* gridSpacingMeters_;

				VectorMath newPos = southWestFrame.getRelativePosition(
															northDistanceMeters,
															eastDistanceMeters,
															LocalDownFrame.RelativePositionOrder.NORTH_THEN_EAST);

				addGridPoint(rowIndex, colIndex, Vertex.getLatLonFromEcf(newPos), number++);
			}
		}
	}
	
	/**
	 * 
	 * @param r
	 * @param c
	 * @return
	 */
	public SpatialGridPoint get(int r, int c) {
		if (r < getRows() && c < getCols()) {
			return grid_[r][c];
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public List<SpatialGridPoint> toList()
	{
		ArrayList<SpatialGridPoint> rtn = new ArrayList<SpatialGridPoint>();

		for ( int  r = 0; r < getRows(); r++) {
			for ( int c = 0; c< getCols(); c++) {
				rtn.add( grid_[r][c]);
			}
		}
		return rtn;
	}

	/**
	 * 
	 * @return
	 */
	public List<SpatialGridPoint> getGridPoints()
	{
		ArrayList<SpatialGridPoint> rtn = new ArrayList<SpatialGridPoint>();

		for ( int  r = 0; r < getRows(); r++) {
			for ( int c = 0; c< getCols(); c++) {
				rtn.add( grid_[r][c]);
			}
		}
		return rtn;
	}

}
