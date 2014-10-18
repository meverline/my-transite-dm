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
import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.vividsolutions.jts.geom.Point;


@XStreamAlias("arrested")
public class Arrested implements Serializable, XmlReadable {

	public static final long serialVersionUID = 1;

	@XStreamAlias("id")
	private long    id_   = 0;

	@XStreamAlias("name")
	private String  name_ = "unknown";
	
	@XStreamAlias("address")
	private String  address_ = "Unknown Address";
	
	@XStreamAlias("age")
	private int     age_ = 0;
	
	@XStreamAlias("point")
	private Point   point_ = null;
	
	@XStreamAlias("distance")
	private double  distance_ = 0.0;
	
	@XStreamAlias("paroledate")
	private Calendar paroleDate_ = null;

	public long getId() {
		return id_;
	}

	protected void setId(long id_) {
		this.id_ = id_;
	}

	public String getName() {
		return name_;
	}

	public void setName(String first_) {
		this.name_ = first_;
	}

	public String getAddress() {
		return address_;
	}

	public void setAddress(String address_) {
		if ( address_.length() > 254 ) {
			this.address_ = address_.substring(0, 254);
		} else {
		   this.address_ = address_;
		}
	}

	public int getAge() {
		return age_;
	}

	public void setAge(int age_) {
		this.age_ = age_;
	}

	public double getDistance() {
		return distance_;
	}

	public void setDistance(double distance_) {
		this.distance_ = distance_;
	}

	public String html() {
		StringBuffer buf = new StringBuffer("<tr><td>"+getName()+ "</td>");

		buf.append("<td>" + getAge()+ "</td>");
		buf.append("<td>");
		buf.append(getAddress()+ "</td>");
		buf.append("</tr>");
		return buf.toString();

	}

	public String asText() {
		StringBuffer buf = new StringBuffer(getName());

		buf.append(", " + getAge());
		buf.append("\n");
		buf.append(getAddress());
		return buf.toString();

	}

	public Point getPoint() {
		return point_;
	}

	public void setPoint(Point point_) {
		this.point_ = point_;
	}

	public void handleObject(Object obj) {
		if ( obj instanceof GeoPoint ) {
			GeoPoint pt = GeoPoint.class.cast(obj);

			// Yahoo points are flooped in the data.
			double tmp = pt.getLatX();
			pt.setLatX(pt.getLonY());
			pt.setLonY(tmp);

			setPoint( pt.asPoint());
		}
	}

	public void save() {
	}

	public Calendar getParoleDate()
	{
		return paroleDate_;
	}

	public void setParoleDate(Calendar paroleDate)
	{
		this.paroleDate_ = paroleDate;
	}

}
