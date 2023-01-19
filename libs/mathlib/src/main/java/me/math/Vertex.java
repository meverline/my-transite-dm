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

package me.math;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@JsonRootName(value="Vertex")
@SuppressWarnings("serial")
@Data
public class Vertex implements Serializable {

	private double latitudeDegress;
	private double longitudeDegress;

	public Vertex()
	{
	}
	
	public Vertex(double lat, double lon) {
		latitudeDegress = lat;
		longitudeDegress = lon;
	}
	
	public Vertex(Point pt) {
		latitudeDegress = pt.getCoordinate().x;
		longitudeDegress = pt.getCoordinate().y;
	}

	public Vertex(Vertex copy)
	{
		latitudeDegress = copy.getLatitudeDegress();
		longitudeDegress = copy.getLongitudeDegress();
	}

	public Vertex(VectorMath vector) {
		Vertex v = Vertex.getLatLonFromEcf(vector);
		latitudeDegress = v.getLatitudeDegress();
		longitudeDegress = v.getLongitudeDegress();
	}

	@JsonIgnore
	public double getLatitudeRadians() {
		return EarthConstants.toRadians(latitudeDegress);
	}

	@JsonIgnore
	public double getLongitudeRadians() {
		return EarthConstants.toRadians(longitudeDegress);
	}

	@JsonIgnore
	public Coordinate toCoordinate() {
		return new Coordinate(getLatitudeDegress(), getLongitudeDegress() );
	}

	@JsonIgnore
	public Point toPoint()
	{
		GeometryFactory factory_  = new GeometryFactory();
		return factory_.createPoint(this.toCoordinate());
	}
	
	@JsonIgnore
	public void formPoint(Point pt)
	{
		latitudeDegress = pt.getCoordinate().x;
		longitudeDegress = pt.getCoordinate().y;
	}

	public boolean equals(Object obj)
	{
		if ( obj instanceof Vertex ) {
			Vertex left = Vertex.class.cast(obj);

			return left.getLatitudeDegress() == getLatitudeDegress() &&
					left.getLongitudeDegress() == getLongitudeDegress();
		}
		return false;

	}
	
	public int hashCode() {
		return this.toPoint().hashCode();
	}

	@JsonIgnore
	public double distanceFrom(Vertex pt)
	{
		return pt.getEcfFromLatLon().distance( this.getEcfFromLatLon());
	}

	@JsonIgnore
	public VectorMath getEcfFromLatLon() {
		return getEcfFromLatLonAlt(0.0);
	}
	
	@JsonIgnore
	public VectorMath getEcfFromLatLonAlt(double altMeters) {
		double latRad = this.getLatitudeRadians();
		double lonRad = this.getLongitudeRadians();

		double sLat = Math.sin(latRad);
		double cLat = Math.cos(latRad);
		double sLon = Math.sin(lonRad);
		double cLon = Math.cos(lonRad);

		//double n = N(latRad, sLat);

		// This Simplifying assumption can be made because this is a strictly round earth model
		double n = EarthConstants.EquatorialRadiusMeters();
		double X = ((n + altMeters) * cLat * cLon);
		double Y = ((n + altMeters) * cLat * sLon);
		double Z = ((n + altMeters) * sLat);

		return new VectorMath(X, Y, Z);
	}

	public static Vertex getLatLonFromEcf(VectorMath positionECF) {
		double p = Math.sqrt(positionECF.getX() * positionECF.getX()
				+ positionECF.getY() * positionECF.getY());

		double latRad, lonRad;

		latRad = Math.atan(positionECF.getZ() / p);
		lonRad = Math.atan2(positionECF.getY(), positionECF.getX());
		return new Vertex(EarthConstants.toDegress(latRad), EarthConstants
				.toDegress(lonRad));

	}
	
	
	
}
