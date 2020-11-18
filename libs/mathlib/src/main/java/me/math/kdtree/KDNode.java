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

package me.math.kdtree;

import me.math.Vertex;
import me.math.grid.SpatialGridPoint;

public class KDNode implements  INode {

  private Vertex center_;
  private SpatialGridPoint point_;
  private INode.Direction direction_;
  private MinBoundingRectangle mbr_;
  private KDNode left_;
  private KDNode right_;
  private INode parent_;
  private int    depth_ = 0;

  public KDNode(SpatialGridPoint loc, INode.Direction dir, INode parent, int depth)
  {
          point_ = loc;
          direction_ = dir;
          center_ = loc.getVertex();
          mbr_ = new MinBoundingRectangle(loc);
          parent_ = parent;
          depth_ = depth;
  }

  /*
   * (non-Javadoc)
   * @see me.math.kdtree.INode#getDepth()
   */
  public int getDepth()
  {
          return depth_;
  }

  /*
   * (non-Javadoc)
   * @see me.math.kdtree.INode#getPoint()
   */
  public SpatialGridPoint getPoint() {
          return point_;
  }

  /* (non-Javadoc)
   * @see ciras.math.INode#getPointVertex()
   */
  public Vertex getPointVertex()
  {
          return center_;
  }

  /*
   * (non-Javadoc)
   * @see me.math.kdtree.INode#getDirection()
   */
  public INode.Direction getDirection() {
          return direction_;
  }

  /*
   * (non-Javadoc)
   * @see me.math.kdtree.INode#getMBR()
   */
  public MinBoundingRectangle getMBR() {
          return mbr_;
  }

  /*
   * (non-Javadoc)
   * @see me.math.kdtree.INode#contains(me.math.Vertex)
   */
  public boolean contains(Vertex pt)
  {
          return getMBR().contains(pt);
  }

  /*
   * (non-Javadoc)
   * @see me.math.kdtree.INode#getLeft()
   */
  public INode getLeft() {
          return left_;
  }

  /* 
   * (non-Javadoc)
   * @see me.math.kdtree.INode#setLeft(me.math.kdtree.INode)
   */
  public void setLeft(INode left_) {
          this.left_ = KDNode.class.cast(left_);
  }

  /* 
   * (non-Javadoc)
   * @see me.math.kdtree.INode#getRight()
   */
  public INode getRight() {
          return right_;
  }

  /* 
   * (non-Javadoc)
   * @see me.math.kdtree.INode#setRight(me.math.kdtree.INode)
   */
  public void setRight(INode right_) {
          this.right_ = KDNode.class.cast(right_);
  }

  /* 
   * (non-Javadoc)
   * @see me.math.kdtree.INode#getParent()
   */
  public INode getParent()
  {
          return parent_;
  }

  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
      return getDirection() + " " + getDepth() + " { " +
              getPoint().getIndex() + " ( " + getPointVertex().getLatitudeDegress() + "," +
              getPointVertex().getLongitudeDegress() + ")";
  }

}