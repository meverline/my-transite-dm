package me.math.kdtree;
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

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.database.nsstore.AbstractDocument;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;


@SuppressWarnings("serial")
@JsonRootName(value = "MinBoundingRectangle")
public class MinBoundingRectangle extends AbstractDocument implements Serializable {

	protected static GeometryFactory factory_ = new GeometryFactory();

	private double bottomlatDegress_ = 90.0;
	private double toplatDegress_ = -90.0;
	private double leftlonDegress_ = 180.0;
	private double rightlonDegress_ = -180.0;

	/**
   * 
   */
	public MinBoundingRectangle() {
	}

	/**
	 * 
	 * @param s
	 */
	public MinBoundingRectangle(SpatialGridPoint s) {
		extend(s);
	}

	/**
	 * 
	 * @param s
	 */
	public MinBoundingRectangle(Vertex s) {
		extend(s);
	}

	/**
	 * 
	 * @param s
	 */
	public MinBoundingRectangle(Polygon s) {
		if (s != null) {
			for (Coordinate coord : s.getCoordinates()) {
				Vertex v = new Vertex(coord.x, coord.y);
				extend(v);
			}
		}
	}

	/**
	 * 
	 * @param location
	 */
	@JsonIgnore
	public void extend(SpatialGridPoint location) {
		if (location != null) {
			bottomlatDegress_ = Math.min(location.getVertex().getLatitudeDegress(), bottomlatDegress_);
			toplatDegress_ = Math.max(location.getVertex().getLatitudeDegress(), toplatDegress_);
			leftlonDegress_ = Math.min(location.getVertex().getLongitudeDegress(), leftlonDegress_);
			rightlonDegress_ = Math.max(location.getVertex().getLongitudeDegress(), rightlonDegress_);
		}
	}

	/**
	 * 
	 * @param location
	 */
	@JsonIgnore
	public void extend(Point location) {
		if (location != null) {
			bottomlatDegress_ = Math.min(location.getX(), bottomlatDegress_);
			toplatDegress_ = Math.max(location.getX(), toplatDegress_);
			leftlonDegress_ = Math.min(location.getY(), leftlonDegress_);
			rightlonDegress_ = Math.max(location.getY(), rightlonDegress_);

		}
	}

	/**
	 * 
	 * @param location
	 */
	@JsonIgnore
	public void extend(Vertex location) {
		if (location != null) {
			bottomlatDegress_ = Math.min(location.getLatitudeDegress(), bottomlatDegress_);
			toplatDegress_ = Math.max(location.getLatitudeDegress(), toplatDegress_);
			leftlonDegress_ = Math.min(location.getLongitudeDegress(), leftlonDegress_);
			rightlonDegress_ = Math.max(location.getLongitudeDegress(), rightlonDegress_);
		}
	}

	/**
	 * 
	 * @param location
	 */
	@JsonIgnore
	public void extend(MinBoundingRectangle location) {
		if (location != null) {
			bottomlatDegress_ = Math.min(location.getBottomLatDegress(),bottomlatDegress_);
			toplatDegress_ = Math.max(location.getTopLatDegress(),toplatDegress_);
			leftlonDegress_ = Math.min(location.getLeftLonDegress(),leftlonDegress_);
			rightlonDegress_ = Math.max(location.getRightLonDegress(),rightlonDegress_);

		}
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public boolean contains(SpatialGridPoint location) {
		return contains(location.getVertex());
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public boolean contains(Point location) {
		return contains(new Vertex(location.getX(), location.getY()));
	}

	/**
	 * 
	 * @param center
	 * @return
	 */
	public boolean contains(Vertex center) {
		return (center.getLatitudeDegress() >= bottomlatDegress_
				&& center.getLatitudeDegress() <= toplatDegress_
				&& center.getLongitudeDegress() >= leftlonDegress_ && center
				.getLongitudeDegress() <= rightlonDegress_);
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Vertex center() {
		double lat = toplatDegress_;
		if (bottomlatDegress_ != toplatDegress_) {
			lat = (bottomlatDegress_ + toplatDegress_) / 2.0;
		}
		double lon = leftlonDegress_;
		if (leftlonDegress_ != rightlonDegress_) {
			lon = (leftlonDegress_ + rightlonDegress_) / 2.0;
		}
		return new Vertex(lat, lon);
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isRectangle() {
		if (bottomlatDegress_ == toplatDegress_) {
			return true;
		}
		if (leftlonDegress_ == rightlonDegress_) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("bottomLatDegress")
	public double getBottomLatDegress() {
		return bottomlatDegress_;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("topLatDegress")
	public double getTopLatDegress() {
		return toplatDegress_;
	}
	
	/**
	 * 
	 * @param toplatDegress_
	 */
	@JsonSetter("topLatDegress")
	public void setToplatDegress(double toplatDegress_) {
		this.toplatDegress_ = toplatDegress_;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("leftLonDegress")
	public double getLeftLonDegress() {
		return leftlonDegress_;
	}
	
	/**
	 * 
	 * @param leftlonDegress_
	 */
	@JsonSetter("leftLonDegress")
	public void setLeftlonDegress(double leftlonDegress_) {
		this.leftlonDegress_ = leftlonDegress_;
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("rightLonDegress")
	public double getRightLonDegress() {
		return rightlonDegress_;
	}
	
	/**
	 * 
	 * @param rightlonDegress_
	 */
	@JsonSetter("rightLonDegress")
	public void setRightlonDegress(double rightlonDegress_) {
		this.rightlonDegress_ = rightlonDegress_;
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonSetter("bottomLatDegress")
	public double getBottomlatDegress_() {
		return bottomlatDegress_;
	}

	/**
	 * 
	 * @param bottomlatDegress
	 */
	@JsonSetter("bottomLatDegress")
	public void setBottomlatDegress(double bottomlatDegress) {
		this.bottomlatDegress_ = bottomlatDegress;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Point getUpperLeft() {
		return factory_.createPoint(new Coordinate(getTopLatDegress(),
				getLeftLonDegress()));
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Point getLowerRight() {
		return  factory_.createPoint(new Coordinate(getBottomLatDegress(),
				getRightLonDegress()));
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Polygon toPolygon() {
		Coordinate [] coords = new Coordinate[5];

		coords[0] = new Coordinate(getTopLatDegress(), getLeftLonDegress());
		coords[1] = new Coordinate(getTopLatDegress(), getRightLonDegress());
		coords[2] = new Coordinate(getBottomLatDegress(), getRightLonDegress());
		coords[3] = new Coordinate(getBottomLatDegress(), getLeftLonDegress());
		coords[4] = coords[0];
		return factory_.createPolygon(factory_.createLinearRing(coords), null);
	}

	@Override
	public String toString() {
		return "MinBoundingRectangle{" +
				"bottomlatDegress_=" + bottomlatDegress_ +
				", toplatDegress_=" + toplatDegress_ +
				", leftlonDegress_=" + leftlonDegress_ +
				", rightlonDegress_=" + rightlonDegress_ +
				'}';
	}


	/**
	 * 
	 * @param top
	 */
	@JsonIgnore
	public void setTop(List<Double> top) {
		this.toplatDegress_ = top.get(0);
		this.leftlonDegress_ = top.get(1);
	}
	
	/**
	 * 
	 * @param top
	 */
	@JsonIgnore
	public void setBottom(List<Double> top) {
		this.bottomlatDegress_ = top.get(0);
		this.rightlonDegress_ = top.get(1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MinBoundingRectangle that = (MinBoundingRectangle) o;
		return Double.compare(that.bottomlatDegress_, bottomlatDegress_) == 0 &&
				Double.compare(that.toplatDegress_, toplatDegress_) == 0 &&
				Double.compare(that.leftlonDegress_, leftlonDegress_) == 0 &&
				Double.compare(that.rightlonDegress_, rightlonDegress_) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bottomlatDegress_, toplatDegress_, leftlonDegress_, rightlonDegress_);
	}

}
