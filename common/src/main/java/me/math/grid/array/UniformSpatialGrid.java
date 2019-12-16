//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright � 2009 by Russ Brasser, Mark Everline and Eric Franklin
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
import me.math.kdtree.KDTree;
import me.math.kdtree.INode.Direction;
import me.math.kdtree.INodeCreator;

import org.locationtech.jts.geom.Point;

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
		AbstractSpatialGridPoint next = null;

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

        double d_latitude = 0.0;
        double d_longitude = 0.0;
        Vertex avgPoint = findAverageLatLon(upperLeft, lowerRight);
		int number = 0;
		for (int rowIndex = 0; rowIndex < this.getRows(); rowIndex++) {
			for (int colIndex = 0; colIndex < this.getCols(); colIndex++) {
				double northDistanceMeters = (double) rowIndex* this.getGridSpacingMeters();
				double eastDistanceMeters = (double) colIndex* this.getGridSpacingMeters();

				VectorMath newPos = southWestFrame.getRelativePosition(
															northDistanceMeters,
															eastDistanceMeters,
															LocalDownFrame.RelativePositionOrder.NORTH_THEN_EAST);

				Vertex pt = Vertex.getLatLonFromEcf(newPos); 
				addGridPoint(rowIndex, colIndex, pt, number++);
				
	            d_latitude += pt.getLatitudeDegress() - avgPoint.getLatitudeDegress();
	            d_longitude += pt.getLongitudeDegress() - avgPoint.getLongitudeDegress();
			}
		}
		
        d_latitude = d_latitude / ( number - 1.0);
        d_longitude = d_longitude / ( number - 1.0);

       this.setCrossCovariance(Math.abs((d_latitude * d_longitude) / ( number - 1.0)));
	}
	
	/**
	 * 
	 * @param r
	 * @param c
	 * @return
	 */
	public AbstractSpatialGridPoint get(int r, int c) {
		if ((r > -1 && r < getRows()) && (c > -1 &&  c < getCols())) {
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
	
	public INode create(AbstractSpatialGridPoint loc, Direction dir, INode parent, int depth) {
		SpatialGridPoint gp = SpatialGridPoint.class.cast(loc);
		gp.setParent(parent);
		gp.initNode(dir, depth);
		return loc;
	}
	
	/**
	 * 
	 * @return
	 */
	public KDTree getTree() {
		return new KDTree(this.getGridPoints(), this);
	}

}
