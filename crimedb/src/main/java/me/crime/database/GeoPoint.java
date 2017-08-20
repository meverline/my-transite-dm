//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.crime.database;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


public class GeoPoint extends XmlReadable {
	
	protected GeometryFactory   factory_  = new GeometryFactory();
	private double 				lat_ = 0.0;
	private double 				lon_ = 0.0;
	private int    				srid_ = 0;
	
	
	public Point asPoint()
	{
		return factory_.createPoint(new Coordinate(getLatX(), getLonY() ));
	}

	public void handleObject(Object obj) {
	}

	public void save() {
	}

	public double getLatX() {
		return lat_;
	}

	public void setLatX(double lat_) {
		this.lat_ = lat_;
	}

	public double getLonY() {
		return lon_;
	}

	public void setLonY(double lon_) {
		this.lon_ = lon_;
	}

	public int getSRID() {
		return srid_;
	}

	public void setSRID(int srid_) {
		this.srid_ = srid_;
	}
}
