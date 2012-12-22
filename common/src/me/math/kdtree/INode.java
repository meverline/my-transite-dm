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
import me.math.grid.SpatialGridPoint;

public interface INode {

  public enum Direction {
          XLAT, YLON
  };

  /**
   * 
   * @return
   */
  public abstract int getDepth();

  /**
   * 
   * @return
   */
  public abstract Vertex getPointVertex();

  /**
   * 
   * @return
   */
  public abstract INode.Direction getDirection();

  /**
   * 
   * @return
   */
  public abstract MinBoundingRectangle getMBR();

  /**
   * 
   * @param pt
   * @return
   */
  public abstract boolean contains(Vertex pt);

  /**
   * 
   * @return
   */
  public abstract INode getLeft();

  /**
   * 
   * @param left_
   */
  public abstract void setLeft(INode left_);

  /**
   * 
   * @return
   */
  public abstract INode getRight();

  /**
   * 
   * @param right_
   */
  public abstract void setRight(INode right_);

  /**
   * 
   * @return
   */
  public abstract INode getParent();

  /**
   * 
   * @return
   */
  public abstract String toString();
    
  /**
   * 
   * @return
   */
  public SpatialGridPoint getPoint() ;

}