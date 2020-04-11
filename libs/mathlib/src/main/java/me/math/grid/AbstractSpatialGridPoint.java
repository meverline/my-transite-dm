package me.math.grid;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
import me.math.grid.array.SpatialGridPoint;
import me.math.grid.data.AbstractDataSample;
import me.math.grid.tiled.TiledSpatialGridPoint;
import me.math.kdtree.INode;
import me.math.kdtree.MinBoundingRectangle;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = SpatialGridPoint.class, name = "SpatialGridPoint"),
		@Type(value = TiledSpatialGridPoint.class, name = "TiledSpatialGridPoint")})
public abstract class AbstractSpatialGridPoint implements INode {
	
	public static final String ROW = "row";
	public static final String COL = "col";
	public static final String INDEX = "index";
	public static final String DATA = "data";
	public static final String DIRECTION = "direction";
	public static final String MBR = "MBR";
	public static final String DEPTH = "depth";

	private int row_ = -1;
	private int col_ = -1;
	private int index_ = -1;
	private AbstractDataSample data_ = null;
	private INode.Direction direction_ = INode.Direction.UNKOWN;
	private MinBoundingRectangle mbr_ = null;
	private int depth_ = 0;


	/**
	 *
	 */
	protected AbstractSpatialGridPoint() {

	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("row")
	public int getRow() {
		return row_;
	}

	/**
	 * 
	 */
	@JsonGetter("col")
	public int getCol() {
		return col_;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
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
	@JsonSetter("row")
	protected void setRow(int row_) {
		this.row_ = row_;
	}

	/**
	 * @param col_ the col_ to set
	 */
	@JsonSetter("col")
	protected void setCol(int col_) {
		this.col_ = col_;
	}

	/**
	 * @param index_ the index_ to set
	 */
	@JsonIgnore
	protected void setIndex(int index_) {
		this.index_ = index_;
	}
	
	/**
	 * @return the data_
	 */
	@JsonGetter("data")
	public AbstractDataSample getData() {
		return data_;
	}

	/**
	 * @param data_ the data_ to set
	 */
	@JsonSetter("data")
	public void setData(AbstractDataSample data_) {
		this.data_ = data_;
	}
	
	public Vertex getPointVertex() {
		return this.getVertex();
	}
	
	@JsonGetter("depth")
	public int getDepth() {
		return this.depth_;
	}
	
	@JsonSetter("depth")
	public void setDepth(int depth) {
		this.depth_ = depth;
	}
	
	@JsonGetter("direction")
	public Direction getDirection() {
		return this.direction_;
	}
	
	@JsonSetter("direction")
	public void setDirection(Direction dir) {
		this.direction_ = dir;
	}

	@JsonGetter("mbr")
	public MinBoundingRectangle getMBR() {
		return this.mbr_;
	}
	
	@JsonSetter("mbr")
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
