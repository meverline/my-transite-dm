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
//
//    Based Upon C# code by Frank Levine and Paul Greene
//    Adapted to Java by Mark Everline

package me.math.grid.array;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import me.math.LocalDownFrame;
import me.math.VectorMath;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGrid;
import me.math.grid.AbstractSpatialGridOverlay;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;

import com.vividsolutions.jts.geom.Point;

public class UniformSpatialGrid extends AbstractSpatialGridOverlay implements INodeCreator {
	
	protected SpatialGridPoint[][] grid_ = null;
	
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
	 * @param gridPt
	 * @return
	 */
	public AbstractSpatialGridPoint getNextGridPoint(AbstractSpatialGridPoint gridPt)
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

		Vertex lowerLeft = new Vertex(lowerRight.getLatitudeDegress(),
									  upperLeft.getLongitudeDegress());

		this.setCols(AbstractSpatialGrid.findNumberOfCols(upperLeft, lowerRight, getGridSpacingMeters()));
		this.setRows(AbstractSpatialGrid.findNumberOfRows(upperLeft, lowerRight, getGridSpacingMeters()));

		createGrid(this.getRows(), this.getCols());
		
		LocalDownFrame southWestFrame = new LocalDownFrame(lowerLeft.getEcfFromLatLon());

		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex++) {
			for (int colIndex = 0; colIndex < this.getCols(); colIndex++) {
				initGridPoit(rowIndex, colIndex);
			}
		}

		int number = 0;
		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex++) {
			for (int colIndex = 0; colIndex < this.getCols(); colIndex++) {
				double northDistanceMeters = (double) rowIndex* this.getGridSpacingMeters();
				double eastDistanceMeters = (double) colIndex* this.getGridSpacingMeters();

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
	public List<AbstractSpatialGridPoint> getGridPoints()
	{
		ArrayList<AbstractSpatialGridPoint> rtn = new ArrayList<AbstractSpatialGridPoint>();

		for ( int  r = 0; r < getRows(); r++) {
			for ( int c = 0; c< getCols(); c++) {
				rtn.add( grid_[r][c]);
			}
		}
		return rtn;
	}
	
	@Override
	public INode create(AbstractSpatialGridPoint loc, Direction dir, INode parent, int depth) {
		SpatialGridPoint gp = SpatialGridPoint.class.cast(loc);
		gp.setParent(parent);
		gp.initNode(dir, depth);
		return loc;
	}

}
