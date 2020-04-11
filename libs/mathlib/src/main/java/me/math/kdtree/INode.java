// CIRAS: Crime Information Retrieval and Analysis System
// Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
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

package me.math.kdtree;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;

public interface INode {

  enum Direction {
    XLAT, YLON, UNKOWN
  }

  /**
   * 
   * @return
   */
  int getDepth();

  /**
   * 
   * @return
   */
  Vertex getPointVertex();

  /**
   * 
   * @return
   */
  INode.Direction getDirection();

  /**
   * 
   * @return
   */
  MinBoundingRectangle getMBR();

  /**
   * 
   * @param pt
   * @return
   */
  boolean contains(Vertex pt);

  /**
   * 
   * @return
   */
  INode getLeft();

  /**
   * 
   * @param left_
   */
  void setLeft(INode left_);

  /**
   * 
   * @return
   */
  INode getRight();

  /**
   * 
   * @param right_
   */
  void setRight(INode right_);

  /**
   * 
   * @return
   */
  INode getParent();

  /**
   * 
   * @return
   */
  String toString();
    
  /**
   * 
   * @return
   */
  AbstractSpatialGridPoint getPoint() ;

}