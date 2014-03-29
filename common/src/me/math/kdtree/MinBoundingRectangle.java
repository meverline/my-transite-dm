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

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


public class MinBoundingRectangle {

  protected static GeometryFactory  factory_  = new GeometryFactory();

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
  public MinBoundingRectangle(AbstractSpatialGridPoint s) {
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
	  if ( s != null ) {
		  for ( Coordinate coord : s.getCoordinates() ) {
			  Vertex v = new Vertex(coord.x, coord.y);
	          extend(v);
		  }
	  }
  }

  /**
   * 
   * @param location
   */
  public void extend(AbstractSpatialGridPoint location) {
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
  public void extend(MinBoundingRectangle location) {
          if (location != null) {
                  bottomlatDegress_ = Math.min(location.getBottomLatDegress(), bottomlatDegress_);
                  toplatDegress_ = Math.max(location.getTopLatDegress(), toplatDegress_);
                  leftlonDegress_ = Math.min(location.getLeftLonDegress(), leftlonDegress_);
                  rightlonDegress_ = Math.max(location.getRightLonDegress(), rightlonDegress_);

          }
  }

  /**
   * 
   * @param location
   * @return
   */
  public boolean contains(AbstractSpatialGridPoint location) {
          return contains(location.getVertex());
  }

  /**
   * 
   * @param location
   * @return
   */
  public boolean contains(Point location) {
          return contains(new Vertex( location.getX(), location.getY()));
  }

  /**
   * 
   * @param center
   * @return
   */
  public boolean contains(Vertex center) {
          return (center.getLatitudeDegress() >= bottomlatDegress_
                          && center.getLatitudeDegress() <= toplatDegress_
                          && center.getLongitudeDegress() >= leftlonDegress_
                          && center.getLongitudeDegress() <= rightlonDegress_ );
  }

  /**
   * 
   * @return
   */
  public Vertex center()
  {
          double lat = toplatDegress_;
          if ( bottomlatDegress_ != toplatDegress_ )  {
                  lat = (bottomlatDegress_ + toplatDegress_) / 2.0;
          }
          double lon = leftlonDegress_;
          if ( leftlonDegress_ != rightlonDegress_ )  {
                  lon = (leftlonDegress_ + rightlonDegress_)/2.0;
          }
          return new Vertex(lat, lon);
  }

  /**
   * 
   * @return
   */
  public boolean isRectangle()
  {
          if ( bottomlatDegress_ == toplatDegress_ )  {
                  return true;
          }
          if ( leftlonDegress_ == rightlonDegress_ )  {
                  return true;
          }
          return false;
  }

  /**
   * 
   * @return
   */
  public double getBottomLatDegress() {
          return bottomlatDegress_;
  }

  /**
   * 
   * @return
   */
  public double getTopLatDegress() {
          return toplatDegress_;
  }

  /**
   * 
   * @return
   */
  public double getLeftLonDegress() {
          return leftlonDegress_;
  }

  /**
   * 
   * @return
   */
  public double getRightLonDegress() {
          return rightlonDegress_;
  }

  /**
   * 
   * @return
   */
  public Point getUpperLeft()
  {
          Point pt = factory_.createPoint(new Coordinate(getTopLatDegress(),getLeftLonDegress() ));
          return pt;
  }

  /**
   * 
   * @return
   */
  public Point getLowerRight()
  {
          Point pt = factory_.createPoint(new Coordinate(getBottomLatDegress(),getRightLonDegress()));
          return pt;
  }
  
  /**
   * 
   * @return
   */
  public Polygon toPolygon()
  {
	   Coordinate coords[] = new Coordinate[5];
	   
	   coords[0] = new Coordinate(getTopLatDegress(),getLeftLonDegress() );
	   coords[1] = new Coordinate(getTopLatDegress(),getRightLonDegress() );
	   coords[2] = new Coordinate(getBottomLatDegress(),getRightLonDegress() );
	   coords[3] = new Coordinate(getBottomLatDegress(),getLeftLonDegress() );
	   coords[4] = coords[0];
	   return factory_.createPolygon(factory_.createLinearRing(coords), null);
  }

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	StringBuilder buf = new StringBuilder();
	
	buf.append("MBR Top: ");
	buf.append( getTopLatDegress());
	buf.append(",");
	buf.append(getLeftLonDegress());
	buf.append(" Bottom: ");
	buf.append( getBottomLatDegress());
	buf.append(",");
	buf.append(getRightLonDegress());
	return buf.toString();
}
  
  

}
