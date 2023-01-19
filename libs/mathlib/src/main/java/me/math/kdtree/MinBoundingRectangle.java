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

import org.apache.commons.math3.util.Precision;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import me.database.nsstore.AbstractDocument;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;

@SuppressWarnings("serial")
@Data
@JsonRootName(value = "MinBoundingRectangle")
public class MinBoundingRectangle extends AbstractDocument implements Serializable {

	protected static GeometryFactory factory_ = new GeometryFactory();

	private double bottomLatDegress = 90.0;
	private double topLatDegress = -90.0;
	private double leftLonDegress = 180.0;
	private double rightLonDegress = -180.0;

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
			bottomLatDegress = Math.min(location.getVertex().getLatitudeDegress(), bottomLatDegress);
			topLatDegress = Math.max(location.getVertex().getLatitudeDegress(), topLatDegress);
			leftLonDegress = Math.min(location.getVertex().getLongitudeDegress(), leftLonDegress);
			rightLonDegress = Math.max(location.getVertex().getLongitudeDegress(), rightLonDegress);
		}
	}

	/**
	 * 
	 * @param location
	 */
	@JsonIgnore
	public void extend(Point location) {
		if (location != null) {
			bottomLatDegress = Math.min(location.getX(), bottomLatDegress);
			topLatDegress = Math.max(location.getX(), topLatDegress);
			leftLonDegress = Math.min(location.getY(), leftLonDegress);
			rightLonDegress = Math.max(location.getY(), rightLonDegress);

		}
	}

	/**
	 * 
	 * @param location
	 */
	@JsonIgnore
	public void extend(Vertex location) {
		if (location != null) {
			bottomLatDegress = Math.min(location.getLatitudeDegress(), getBottomLatDegress());
			topLatDegress = Math.max(location.getLatitudeDegress(), getTopLatDegress());
			leftLonDegress = Math.min(location.getLongitudeDegress(), getLeftLonDegress());
			rightLonDegress = Math.max(location.getLongitudeDegress(), getRightLonDegress());
		}
	}

	/**
	 * 
	 * @param location
	 */
	@JsonIgnore
	public void extend(MinBoundingRectangle location) {
		if (location != null) {
			bottomLatDegress = Math.min(location.getBottomLatDegress(),getBottomLatDegress());
			topLatDegress = Math.max(location.getTopLatDegress(),getTopLatDegress());
			leftLonDegress = Math.min(location.getLeftLonDegress(),getLeftLonDegress());
			rightLonDegress = Math.max(location.getRightLonDegress(),getRightLonDegress());

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
		return (center.getLatitudeDegress() >= getBottomLatDegress()
				&& center.getLatitudeDegress() <= getTopLatDegress()
				&& center.getLongitudeDegress() >= getLeftLonDegress() && center
				.getLongitudeDegress() <= getRightLonDegress());
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Vertex center() {
		double lat = getTopLatDegress();
		if (getBottomLatDegress() != getTopLatDegress()) {
			lat = (getBottomLatDegress() + getTopLatDegress()) / 2.0;
		}
		double lon = getLeftLonDegress();
		if (getLeftLonDegress() != getRightLonDegress()) {
			lon = (getLeftLonDegress() + getRightLonDegress()) / 2.0;
		}
		return new Vertex(lat, lon);
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isRectangle() {
		if (getBottomLatDegress() == getTopLatDegress() ) {
			return true;
		}
		if (getLeftLonDegress() == getRightLonDegress() ) {
			return true;
		}
		return false;
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

	/**
	 * 
	 * @param top
	 */
	@JsonIgnore
	public void setTop(List<Double> top) {
		this.setTopLatDegress(top.get(0));
		this.setLeftLonDegress(top.get(1));
	}
	
	/**
	 * 
	 * @param top
	 */
	@JsonIgnore
	public void setBottom(List<Double> top) {
		this.setBottomLatDegress(top.get(0));
		this.setRightLonDegress(top.get(1));
	}
	
	public boolean equals(Object obj) {
		
		if ( obj instanceof MinBoundingRectangle ) {
			double epsilon = 0.000001d;
			MinBoundingRectangle rhs = MinBoundingRectangle.class.cast(obj);
			return Precision.equals(getBottomLatDegress(),rhs.getBottomLatDegress(),epsilon) &&
				   Precision.equals(getRightLonDegress(), rhs.getRightLonDegress(),epsilon) &&
				   Precision.equals(getTopLatDegress(), rhs.getTopLatDegress(),epsilon) &&
				   Precision.equals(getLeftLonDegress(), rhs.getLeftLonDegress(),epsilon);
		}
		return false;
	}
	
	public int hashCode() {
		return this.getUpperLeft().hashCode() + 
			   this.getLowerRight().hashCode();
	}


}
