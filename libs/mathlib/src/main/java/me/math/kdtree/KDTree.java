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

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;

public class KDTree {

	private INode root_ = null;
	private GridComparator comparator_ = null;
	private Log log = LogFactory.getLog(KDTree.class);

	/**
	 * 
	 * @param aList
	 */
	public KDTree(List<AbstractSpatialGridPoint> aList, INodeCreator creator) {
		comparator_ = new GridComparator(INode.Direction.XLAT);
		aList.sort(comparator_);

		root_ = insertNode(aList, INode.Direction.XLAT, root_, 0, creator);
	}

	public final INode getRootNode() {
		return this.root_;
	}

	/**
	 * 
	 * @param root
	 */
	public KDTree(INode root) {
		comparator_ = new GridComparator(INode.Direction.XLAT);
		root_ = root;
	}

	/**
	 * 
	 * @param aList
	 * @param direction
	 * @param parent
	 * @param depth
	 * @return
	 */
	private INode insertNode(List<AbstractSpatialGridPoint> aList,
							 INode.Direction direction, 
							 INode parent, 
							 int depth, 
							 INodeCreator creator) {

		if (aList.isEmpty()) {
			return null;
		}

		// Note using interger math other wise the midpoint calculation fails eventually 3/2 > 1.5  vs 1
		int midpoint = (int) Math.floor((aList.size() / 2) + 0.5f);

		AbstractSpatialGridPoint pt = aList.get(midpoint);
		INode node = creator.create(pt, direction, parent, depth);
		
		INode.Direction change = direction;
		if (direction == INode.Direction.XLAT) {
			change = INode.Direction.YLON;
		} else {
			change = INode.Direction.XLAT;
		}

		List<AbstractSpatialGridPoint> leftList = new ArrayList<>();

		for (int n = 0; n < midpoint; n++) {
			leftList.add(aList.get(n));
		}
		
		comparator_.setDirection(change);
		leftList.sort(comparator_);
		node.setLeft(insertNode(leftList, change, node, depth+1, creator));
		if (node.getLeft() != null) {
			node.getMBR().extend(node.getLeft().getMBR());
		}

		List<AbstractSpatialGridPoint> rightList = new ArrayList<>();

		for (int n = midpoint + 1; n < aList.size(); n++) {
			rightList.add(aList.get(n));
		}
		
		comparator_.setDirection(change);
		rightList.sort(comparator_);
		node.setRight(insertNode(rightList, change, node, depth + 1, creator));
		if (node.getRight() != null) {
			node.getMBR().extend(node.getRight().getMBR());
		}

		return node;
	}

	/**
	 * 
	 * @param node
	 * @param file
	 * @param depth
	 */
	protected void dump(INode node, java.io.PrintWriter file, int depth) {
		if (node == null) {
			return;
		}

		for (int ndx = 0; ndx < depth; ndx++) {
			file.print(" ");
		}
		file.println(node.toString());
		dump(node.getLeft(), file, depth + 3);
		dump(node.getRight(), file, depth + 3);
	}

	/**
	 * 
	 * @param file
	 */
	public void dump(java.io.PrintWriter file) {
		dump(root_, file, 0);
	}

	/**
	 * 
	 * @param file
	 */
	public void dump(String file) {
		PrintWriter ps = null;
		try {
		    ps = new PrintWriter(new FileOutputStream(file));
			dump(root_, ps, 0);
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			log.error(ex);
		}
		if ( ps != null ) {
			ps.close();
		}
	}

	/**
	 * 
	 * @param node
	 * @param search
	 */
	protected void find(INode node, IKDSearch search) {
		if (node == null) {
			return;
		}

		if (node.contains(search.getVertex())) {

			search.compare(node);
			if (search.endSearch(node)) {
				return;
			}

			Vertex vertex = search.getVertex();
			if (node.getDirection() == INode.Direction.XLAT) {
				if (vertex.getLatitudeDegress() < node.getPointVertex()
						.getLatitudeDegress()) {
					find(node.getLeft(), search);
				} else {
					find(node.getRight(), search);
				}
			} else {
				if (vertex.getLongitudeDegress() < node.getPointVertex()
						.getLongitudeDegress()) {
					find(node.getLeft(), search);
				} else {
					find(node.getRight(), search);
				}
			}
		}

	}

	/**
	 * 
	 * @param node
	 * @param nodeSearch
	 */
	protected void search(INode node, IKDSearch nodeSearch) {
		if (node == null) {
			return;
		}

		if (node.contains(nodeSearch.getVertex())) {
			nodeSearch.compare(node);
			if (nodeSearch.endSearch(node)) {
				return;
			}
			search(node.getLeft(), nodeSearch);
			search(node.getRight(), nodeSearch);
		}
	}

	/**
	 * 
	 * @param node
	 * @param search
	 */
	protected void searchStats(INode node, IKDSearch search) {
		if (node == null) {
			return;
		}

		search.compare(node);
		if (search.endSearch(node)) {
			return;
		}

		searchStats(node.getLeft(), search);
		searchStats(node.getRight(), search);

	}

	/**
	 * 
	 * @param nodeSearch
	 * @return
	 */
	public List<AbstractSpatialGridPoint> search(IKDSearch nodeSearch) {
		if (root_.contains(nodeSearch.getVertex())) {
			search(root_, nodeSearch);
		}
		return nodeSearch.getResults();
	}

	/**
	 * 
	 * @param search
	 * @return
	 */
	public List<AbstractSpatialGridPoint> find(IKDSearch search) {
		if (root_.contains(search.getVertex())) {
			find(root_, search);
		}
		return search.getResults();
	}

	/**
	 * 
	 * @param search
	 * @return
	 */
	public List<AbstractSpatialGridPoint> searchStats(IKDSearch search) {
		searchStats(root_, search);
		return search.getResults();
	}

  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////

  public class GridComparator implements Comparator<AbstractSpatialGridPoint> {

          private INode.Direction direction_;

          public GridComparator( INode.Direction dir)
          {
                  direction_ = dir;
          }

          private INode.Direction getDirection() {
                  return direction_;
          }

          public void setDirection(INode.Direction direction_) {
                  this.direction_ = direction_;
          }

          public int latCompare(Vertex left, Vertex right) {
                  if (left.getLatitudeDegress() < right.getLatitudeDegress()) {
                          return -1;
                  } else if (left.getLatitudeDegress() > right.getLatitudeDegress()) {
                          return 1;
                  }
                  return 0;
          }

          public int lonCompare(Vertex left, Vertex right) {
                  if (left.getLongitudeDegress() < right.getLongitudeDegress()) {
                          return -1;
                  } else if (left.getLongitudeDegress() > right.getLongitudeDegress()) {
                          return 1;
                  }
                  return 0;
          }

          public int compare(AbstractSpatialGridPoint o1, AbstractSpatialGridPoint o2)
          {
                  Vertex left = o1.getVertex();
                  Vertex right = o2.getVertex();

                  if ( this.getDirection() == INode.Direction.XLAT) {
                          return latCompare(left, right);
                  } else {
                          return lonCompare(left, right);
                  }
          }

  }

}