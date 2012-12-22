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

import me.math.Vertex;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("SpatialGridPoint")
public class SpatialGridPoint {

	@XStreamAlias("row")
	private int row_ = -1;
	@XStreamAlias("col")
	private int col_ = -1;
	@XStreamAlias("corner")
	private Vertex corner_ = null;
	@XStreamOmitField
	private int index_ = -1;
	@XStreamOmitField
	private UniformSpatialGrid grid_ = null;
	@XStreamAlias("Data")
	private SpatialGridData data_ = null;
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param corner
	 * @param index
	 * @param grid
	 */
	public SpatialGridPoint(int row, int col, Vertex corner, int index, UniformSpatialGrid grid) {

		row_ = row;
		col_ = col;
		corner_ = corner;
		index_ = index;
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
	public Vertex getVertex()
	{
		return new Vertex(corner_);
	}

	/**
	 * @return the data_
	 */
	public SpatialGridData getData() {
		return data_;
	}

	/**
	 * @param data_ the data_ to set
	 */
	public void setData(SpatialGridData data_) {
		this.data_ = data_;
	}
	
	

}
