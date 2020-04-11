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

package me.math.grid.array;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.INode;

public class SpatialGridPoint extends AbstractSpatialGridPoint{

	private Vertex corner_ = null;
	private UniformSpatialGrid grid_ = null;
	private INode left_ = null;
	private INode right_ = null;
	private INode parent_ = null;
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param corner
	 * @param index
	 * @param grid
	 */
	public SpatialGridPoint(int row, int col, Vertex corner, int index, UniformSpatialGrid grid) {

		this.setRow(row);
		this.setCol(col);
		this.setCorner(corner);
		this.setIndex(index);
		grid_ = grid;
	}

	/**
	 * 
	 * @return
	 */
	public UniformSpatialGrid Grid()
	{
		return grid_;
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonGetter("corner")
	public Vertex getVertex()
	{
		return new Vertex(corner_);
	}
	
	/**
	 * @param corner_ the corner_ to set
	 */
	@JsonSetter("corner")
	protected void setCorner(Vertex corner_) {
		this.corner_ = corner_;
	}

	/**
	 * 
	 * @return
	 */
	public String dumpInfo() {
		return "\t[" + this.getRow() + ", " + this.getCol() + "]";
	}

	/* (non-Javadoc)
	 * @see me.math.kdtree.INode#getLeft()
	 */
	public INode getLeft() {
		return this.left_;
	}

	/* (non-Javadoc)
	 * @see me.math.kdtree.INode#setLeft(me.math.kdtree.INode)
	 */
	public void setLeft(INode left) {
		this.left_ = left;
	}

	/* (non-Javadoc)
	 * @see me.math.kdtree.INode#getRight()
	 */
	public INode getRight() {
		return this.right_;
	}

	/* (non-Javadoc)
	 * @see me.math.kdtree.INode#setRight(me.math.kdtree.INode)
	 */
	public void setRight(INode right) {
		this.right_ = right;
	}

	/* (non-Javadoc)
	 * @see me.math.kdtree.INode#getParent()
	 */
	public INode getParent() {
		return this.parent_;
	}
	
	public void setParent(INode index) 
	{
		this.parent_ = index;
	}

	/* (non-Javadoc)
	 * @see me.math.kdtree.INode#getPoint()
	 */
	public AbstractSpatialGridPoint getPoint() {
		return this;
	}
	
	

}