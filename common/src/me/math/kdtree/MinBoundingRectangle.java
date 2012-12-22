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
import me.math.grid.SpatialGridPoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


public class MinBoundingRectangle {

  protected static GeometryFactory  factory_  = new GeometryFactory();

  private double bottomlatDegress_ = 90.0;
  private double toplatDegress_ = -90.0;
  private double leftlonDegress_ = 180.0;
  private double rightlonDegress_ = -180.0;

  public MinBoundingRectangle() {
  }


  public MinBoundingRectangle(SpatialGridPoint s) {
          extend(s);
  }

  public MinBoundingRectangle(Vertex s) {
          extend(s);
  }

  public void extend(SpatialGridPoint location) {
          if (location != null) {
                  bottomlatDegress_ = Math.min(location.getVertex().getLatitudeDegress(), bottomlatDegress_);
                  toplatDegress_ = Math.max(location.getVertex().getLatitudeDegress(), toplatDegress_);
                  leftlonDegress_ = Math.min(location.getVertex().getLongitudeDegress(), leftlonDegress_);
                  rightlonDegress_ = Math.max(location.getVertex().getLongitudeDegress(), rightlonDegress_);
          }
  }

  public void extend(Point location) {
          if (location != null) {
                  bottomlatDegress_ = Math.min(location.getX(), bottomlatDegress_);
                  toplatDegress_ = Math.max(location.getX(), toplatDegress_);
                  leftlonDegress_ = Math.min(location.getY(), leftlonDegress_);
                  rightlonDegress_ = Math.max(location.getY(), rightlonDegress_);
 
          }
  }

  public void extend(Vertex location) {
          if (location != null) {
                  bottomlatDegress_ = Math.min(location.getLatitudeDegress(), bottomlatDegress_);
                  toplatDegress_ = Math.max(location.getLatitudeDegress(), toplatDegress_);
                  leftlonDegress_ = Math.min(location.getLongitudeDegress(), leftlonDegress_);
                  rightlonDegress_ = Math.max(location.getLongitudeDegress(), rightlonDegress_);

          }
  }


  public void extend(MinBoundingRectangle location) {
          if (location != null) {
                  bottomlatDegress_ = Math.min(location.getBottomLatDegress(), bottomlatDegress_);
                  toplatDegress_ = Math.max(location.getTopLatDegress(), toplatDegress_);
                  leftlonDegress_ = Math.min(location.getLeftLonDegress(), leftlonDegress_);
                  rightlonDegress_ = Math.max(location.getRightLonDegress(), rightlonDegress_);

          }
  }

  public boolean contains(SpatialGridPoint location) {
          return contains(location.getVertex());
  }

  public boolean contains(Point location) {
          return contains(new Vertex( location.getX(), location.getY()));
  }

  public boolean contains(Vertex center) {
          return (center.getLatitudeDegress() >= bottomlatDegress_
                          && center.getLatitudeDegress() <= toplatDegress_
                          && center.getLongitudeDegress() >= leftlonDegress_
                          && center.getLongitudeDegress() <= rightlonDegress_ );
  }

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

  public double getBottomLatDegress() {
          return bottomlatDegress_;
  }

  public double getTopLatDegress() {
          return toplatDegress_;
  }

  public double getLeftLonDegress() {
          return leftlonDegress_;
  }

  public double getRightLonDegress() {
          return rightlonDegress_;
  }

  public Point getUpperLeft()
  {
          Point pt = factory_.createPoint(new Coordinate(getTopLatDegress(),getLeftLonDegress() ));
          return pt;
  }

  public Point getLowerRight()
  {
          Point pt = factory_.createPoint(new Coordinate(getBottomLatDegress(),getRightLonDegress()));
          return pt;
  }

}
