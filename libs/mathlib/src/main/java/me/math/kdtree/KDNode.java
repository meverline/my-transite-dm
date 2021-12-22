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

import lombok.Data;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;

@Data
public class KDNode implements INode {

    private Vertex pointVertex; // center
    private SpatialGridPoint point;
    private INode.Direction direction;
    private MinBoundingRectangle MBR;
    private KDNode left;
    private KDNode right;
    private INode parent;
    private int depth = 0;

    public KDNode(SpatialGridPoint loc, INode.Direction dir, INode parent, int depth) {
        this.point = loc;
        this.direction = dir;
        this.pointVertex = loc.getVertex();
        this.MBR = new MinBoundingRectangle(loc);
        this.parent = parent;
        this.depth = depth;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getDirection() + " " + getDepth() + " { " +
                getPoint().getIndex() + " ( " + getPointVertex().getLatitudeDegress() + "," +
                getPointVertex().getLongitudeDegress() + ")";
    }

    /*
     * (non-Javadoc)
     * @see me.math.kdtree.INode#contains(me.math.Vertex)
     */
    public boolean contains(Vertex pt) {
        return getMBR().contains(pt);
    }

    /*
     * (non-Javadoc)
     * @see me.math.kdtree.INode#setLeft(me.math.kdtree.INode)
     */
    public void setLeft(INode left) {
        this.left = KDNode.class.cast(left);
    }

    /*
     * (non-Javadoc)
     * @see me.math.kdtree.INode#setRight(me.math.kdtree.INode)
     */
    public void setRight(INode right) {
        this.right = KDNode.class.cast(right);
    }

}