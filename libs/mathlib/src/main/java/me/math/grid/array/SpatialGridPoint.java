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

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;

public class SpatialGridPoint extends AbstractSpatialGridPoint{

	private final transient UniformSpatialGrid grid_;

	/**
	 * 
	 * @param row
	 * @param col
	 * @param corner
	 * @param index
	 * @param grid
	 */
	public SpatialGridPoint(int row, int col, Vertex corner, int index, UniformSpatialGrid grid) {
		super(row, col, corner, index);
		grid_ = grid;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public UniformSpatialGrid Grid()
	{
		return grid_;
	}
	
	/**
	 * 
	 * @return
	 */
	public String dumpInfo() {
		return "\t[" + this.getRow() + ", " + this.getCol() + "]";
	}


}
