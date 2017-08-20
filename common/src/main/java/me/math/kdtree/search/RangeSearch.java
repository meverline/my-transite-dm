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

package me.math.kdtree.search;

import java.util.ArrayList;
import java.util.List;

import me.math.EarthConstants;
import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import me.math.kdtree.IKDSearch;
import me.math.kdtree.INode;

import com.vividsolutions.jts.geom.Point;


public class RangeSearch implements IKDSearch {

	private List<AbstractSpatialGridPoint> list_;
	private double distanceInMeters_;
	private Vertex point_;

	/**
	 * 
	 * @param point
	 * @param distanceInMeters
	 */
	public RangeSearch(Vertex point, double distanceInMeters) {
		setList(new ArrayList<AbstractSpatialGridPoint>());
		setPoint(point);
		setDistanceInMeters(distanceInMeters);
	}

	/**
	 * 
	 * @param pt
	 * @param distanceInMeters
	 */
	public RangeSearch(Point pt, double distanceInMeters) {
		setList(new ArrayList<AbstractSpatialGridPoint>());
		setPoint(new Vertex(pt.getX(), pt.getY()));
		setDistanceInMeters(distanceInMeters);
	}

	/**
   * 
   */
	public void reset() {
		setList(new ArrayList<AbstractSpatialGridPoint>());
	}

	/**
   * 
   */
	public void compare(INode point) {
		double distance = EarthConstants.distanceMeters(getPoint(),
														point.getPointVertex());
		if (distance <= getDistanceInMeters()) {
			getList().add(point.getPoint());
		}
	}

	/**
   * 
   */
	public List<AbstractSpatialGridPoint> getResults() {
		return getList();
	}

	/**
   * 
   */
	public boolean endSearch(INode node) {
		return false;
	}

	/**
   * 
   */
	public Vertex getVertex() {
		return getPoint();
	}

	/**
	 * 
	 * @return
	 */
	protected double getDistanceInMeters() {
		return distanceInMeters_;
	}

	/**
	 * 
	 * @param distanceInMeters_
	 */
	public void setDistanceInMeters(double distanceInMeters_) {
		this.distanceInMeters_ = distanceInMeters_;
	}

	/**
	 * 
	 * @return
	 */
	protected List<AbstractSpatialGridPoint> getList() {
		return list_;
	}

	/**
	 * 
	 * @param list_
	 */
	protected void setList(List<AbstractSpatialGridPoint> list_) {
		this.list_ = list_;
	}

	/**
	 * 
	 * @return
	 */
	protected Vertex getPoint() {
		return point_;
	}

	/**
	 * 
	 * @param point_
	 */
	public void setPoint(Vertex point_) {
		this.point_ = point_;
	}
	
	public void setPoint(Point pt) {
		this.point_ = new Vertex(pt.getX(), pt.getY());
	}

}