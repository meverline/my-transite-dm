//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
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

import java.io.Serializable;

import me.math.Vertex;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.vividsolutions.jts.geom.Point;


@XStreamAlias("address")
public class Address implements Serializable, XmlReadable {

	public static final long serialVersionUID = 1;

	/**
	 * 2283 SRID is:  ( found using spatial_ref in database note: SRID is the projection)
	 * PROJCS["NAD83 / Virginia North (ftUS)",GEOGCS["NAD83,
	 * DATUM["North_American_Datum_1983",SPHEROID["GRS 1980",6378137,298.257222101,AUTHORITY["EPSG","7019"]],
	 * AUTHORITY["EPSG","6269"]],
	 * PRIMEM["Greenwich",0,
	 * AUTHORITY["EPSG","8901"]],
	 * UNIT["degree",0.01745329251994328,
	 * AUTHORITY["EPSG","9122"]],
	 * AUTHORITY["EPSG","4269"]],
	 * PROJECTION["Lambert_Conformal_Conic_2SP"],
	 * PARAMETER["standard_parallel_1",39.2],
	 * PARAMETER["standard_parallel_2",38.03333333333333],
	 * PARAMETER["latitude_of_origin",37.66666666666666],
	 * PARAMETER["central_meridian",-78.5],
	 * PARAMETER["false_easting",11482916.667],
	 * PARAMETER["false_northing",6561666.667],
	 * UNIT["US survey foot",0.3048006096012192,
	 * AUTHORITY["EPSG","9003"]],
	 * AUTHORITY["EPSG","2283"]]"
	 */

	@XStreamAlias("id")
	private long id_ = 0;

	@XStreamAlias("location")
	private String location_ = "";

	@XStreamAlias("city")
	private String city_ = "";
	
	@XStreamAlias("state")
	private String state_ = "";
	
	@XStreamAlias("zipcode")
	private String zip_ = "";
	
	@XStreamAlias("service")
	private String service_ = "";
	
	@XStreamAlias("accuracy")
	private int    accuracy_ = 0;
	
	@XStreamAlias("point")
	private Point  point_ = null;

	/**
	 *
	 * @return
	 */
	public long getId() {
		return id_;
	}

	/**
	 *
	 * @param id_
	 */
	protected void setId(long id_) {
		this.id_ = id_;
	}

	/**
	 *
	 * @return
	 */
	public String getLocation() {
		return location_;
	}

	/*
	 *
	 */
    public void setLocation(String address_) {
        String location = address_;
        if ( address_.length() > 255 ) {
            location = address_.substring(0,254);
        }
        this.location_ = location;
    }

    /**
	 *
	 * @return
	 */
	public Point getPoint() {
		return point_;
	}

	/**
	 *
	 * @param point_
	 */
	public void setPoint(Point point_) {
		this.point_ = point_;
	}

    /**
     * 
     * @return
     */
	public String getFullAddress()
	{
		StringBuffer buf = new StringBuffer();

		buf.append(getLocation() +  ",");
		buf.append(getCity() + " ");
		buf.append(getState() + " ");
		buf.append(getZipCode() + " ");

		return buf.toString();
	}

	/**
	 * @return the city_
	 */
	public String getCity() {
		return city_;
	}

	/**
	 * @param city_ the city_ to set
	 */
	public void setCity(String city_) {
		this.city_ = city_;
	}

	/**
	 * @return the state_
	 */
	public String getState() {
		return state_;
	}

	/**
	 * @param state_ the state_ to set
	 */
	public void setState(String state_) {
		this.state_ = state_;
	}

	/**
	 * @return the zip_
	 */
	public String getZipCode() {
		return zip_;
	}

	/**
	 * @param zip_ the zip_ to set
	 */
	public void setZipCode(String zip_) {
		this.zip_ = zip_;
	}

	/**
	 * @return the accuracy_
	 */
	public int getAccuracy() {
		return accuracy_;
	}

	/**
	 * @param accuracy_ the accuracy_ to set
	 */
	public void setAccuracy(int accuracy_) {
		this.accuracy_ = accuracy_;
	}

	/**
	 * @return the service_
	 */
	public String getService() {
		return service_;
	}

	/**
	 * @param service_ the service_ to set
	 */
	public void setService(String service_) {
		this.service_ = service_;
	}

	/**
	 *
	 * @param from
	 */
	public void setAddress(Address from) {
		setService( from.getService());
		setId( from.getId());
		setCity( from.getCity());
		setAccuracy( from.getAccuracy());
		setPoint( from.getPoint());
		setZipCode( from.getZipCode());
		setState( from.getState());
		setLocation( from.getLocation());
	}

	/**
	 * 
	 * @return
	 */
	public Vertex getPointAsVertex()
	{
		return new Vertex( getPoint().getX(), getPoint().getY());
	}

	/**
	 * 
	 */
	public void handleObject(Object obj) {
		if ( obj instanceof GeoPoint ) {
			GeoPoint pt = GeoPoint.class.cast(obj);

			// Everything other then yahoo points are flooped in the data.
			if ( this.service_.toLowerCase().compareTo("yahoo") != 0) {
				// but only some are flipped
				if ( pt.getLonY() > 1 ) {
					double tmp = pt.getLatX();
					pt.setLatX(pt.getLonY());
					pt.setLonY(tmp);
				}
			}
			setPoint( pt.asPoint());
		}
	}

	/**
	 * 
	 */
	public void save() {
	}

}
