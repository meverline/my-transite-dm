package me.math.grid;

//CIRAS: Crime Information Retrieval and Analysis System
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

import me.math.Vertex;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.tiled.IGridDocument;
import me.math.kdtree.INode;
import me.math.kdtree.MinBoundingRectangle;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("SpatialGridPoint")
public abstract class AbstractSpatialGridPoint implements INode {
	
	public static final String ROW = "row";
	public static final String COL = "col";
	public static final String INDEX = "index";
	public static final String DATA = "data";
	public static final String DIRECTION = "direction";
	public static final String MBR = "MBR";
	public static final String DEPTH = "depth";

	@XStreamAlias(AbstractSpatialGridPoint.ROW)
	private int row_ = -1;
	@XStreamAlias(AbstractSpatialGridPoint.COL)
	private int col_ = -1;
	@XStreamOmitField
	private int index_ = -1;
	@XStreamAlias(AbstractSpatialGridPoint.DATA)
	private AbstractDataSample data_ = null;
	@XStreamAlias(AbstractSpatialGridPoint.DIRECTION)
	private INode.Direction direction_ = INode.Direction.UNKOWN;
	@XStreamAlias(IGridDocument.MBR)
	private MinBoundingRectangle mbr_ = null;
	@XStreamAlias(AbstractSpatialGridPoint.DEPTH)
	private int depth_ = 0;
	
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param corner
	 * @param index
	 * @param grid
	 */
	protected AbstractSpatialGridPoint() {

	}

	/**
	 * 
	 * @return
	 */
	public int getRow() {
		return row_;
	}

	/**
	 * 
	 */
	public int getCol() {
		return col_;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndex() {
		return index_;
	}

	/**
	 * 
	 * @return
	 */
	public String dumpInfo() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("\t[" + this.getRow() + ", " + this.getCol() + "]");
		return buffer.toString();
	}

	/**
	 * 
	 * @return
	 */
	public abstract Vertex getVertex();
	
	/**
	 * @param corner_ the corner_ to set
	 */
	protected abstract void setCorner(Vertex corner_);

	/**
	 * @param row_ the row_ to set
	 */
	protected void setRow(int row_) {
		this.row_ = row_;
	}

	/**
	 * @param col_ the col_ to set
	 */
	protected void setCol(int col_) {
		this.col_ = col_;
	}



	/**
	 * @param index_ the index_ to set
	 */
	protected void setIndex(int index_) {
		this.index_ = index_;
	}
	
	/**
	 * @return the data_
	 */
	public AbstractDataSample getData() {
		return data_;
	}

	/**
	 * @param data_ the data_ to set
	 */
	public void setData(AbstractDataSample data_) {
		this.data_ = data_;
	}
	
	public Vertex getPointVertex() {
		return this.getVertex();
	}
	
	public int getDepth() {
		return this.depth_;
	}
	
	public void setDepth(int depth) {
		this.depth_ = depth;
	}
	
	public Direction getDirection() {
		return this.direction_;
	}
	
	public void setDirection(Direction dir) {
		this.direction_ = dir;
	}

	public MinBoundingRectangle getMBR() {
		return this.mbr_;
	}
	
	public void setMBR(MinBoundingRectangle mbr) {
		this.mbr_ = mbr;
	}

	public boolean contains(Vertex pt) {
		return getMBR().contains(pt);
	}
	
	/**
	 * 
	 * @param dir
	 * @param depth
	 */
	public void initNode(Direction dir, int depth)
	{
		setDirection(dir);
		setDepth(depth);
		setMBR( new MinBoundingRectangle( this.getPointVertex()));
	}
	
	 /* 
	   * (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */
	public String toString()
	{
       StringBuffer buf = new StringBuffer();

       buf.append(getDirection() + " ");
       buf.append(getDepth());
       buf.append(" { " + getPoint().getIndex());
       if ( this.getPointVertex() != null ) {
    	   		buf.append(" ( " + getPointVertex().getLatitudeDegress() +"," +
                               getPointVertex().getLongitudeDegress() + ")");
       }
       return buf.toString();
	}
	
}
