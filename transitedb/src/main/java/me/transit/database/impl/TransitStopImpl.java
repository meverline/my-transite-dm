package me.transit.database.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Point;

import me.transit.database.TransitStop;

@Entity
@Table(name = "tran_stop")
public class TransitStopImpl extends TransitDateImpl implements TransitStop {

	@XStreamOmitField
	private static final long serialVersionUID = 1L;

	@Column(name = "CODE")
	@XStreamAlias("code")
	private String code = "";

	@Column(name = "NAME", nullable = false)
	@XStreamAlias("name")
	private String name = null;

	@Column(name = "DESCRIPTION")
	@XStreamAlias("desc")
	private String desc = "";

	@Column(name = "LOCATION", columnDefinition = "Geometry")
	@XStreamAlias("location")
	@XStreamConverter(me.database.PointConverter.class)
	private Point location = null;

	@Column(name = "ZONE")
	@XStreamAlias("zoneId")
	private String zoneId = null;

	@Column(name = "URL")
	@XStreamAlias("url")
	private String url = "";

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	@XStreamAlias("locationType")
	private LocationType locationType = LocationType.UNKNOW;

	@Column(name = "PARENT_STATION")
	@XStreamAlias("parentStation")
	private int parentStation = -1;

	private boolean wheelchairBoarding;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the desc
	 */

	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @return the zoneId
	 */
	public String getZoneId() {
		return zoneId;
	}

	/**
	 * @param zoneId
	 *            the zoneId to set
	 */
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the locationType
	 */
	public LocationType getLocationType() {
		return locationType;
	}

	/**
	 * @param locationType
	 *            the locationType to set
	 */
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	/**
	 * @return the parentStation
	 */
	public int getParentStation() {
		return parentStation;
	}

	/**
	 * @param parentStation
	 *            the parentStation to set
	 */
	public void setParentStation(int parentStation) {
		this.parentStation = parentStation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.impl.TransitDateImpl#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("TransiteStop: {" + super.toString() + "}");

		builder.append("code: " + this.getCode());
		builder.append("\n");
		builder.append("name: " + this.getName());
		builder.append("\n");
		builder.append("desc: " + this.getCode());
		builder.append("\n");
		builder.append("location: " + this.getName());
		builder.append("\n");
		builder.append("ZoneId: " + this.getCode());
		builder.append("\n");
		builder.append("url: " + this.getName());
		builder.append("\n");
		builder.append("locationType: " + this.getLocationType());
		builder.append("\n");
		builder.append("parentStation: " + this.getParentStation());
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#setStopTimezone(java.lang.String)
	 */
	public void setStopTimezone(String value) {
		this.getAgency().setTimezone(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#getStopTimezone()
	 */
	public String getStopTimezone() {
		return this.getAgency().getTimezone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#getWheelchairBoarding()
	 */
	public boolean getWheelchairBoarding() {
		return wheelchairBoarding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#setWheelchairBoarding(boolean)
	 */
	public void setWheelchairBoarding(boolean value) {
		wheelchairBoarding = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitData#valid()
	 */
	public boolean valid() {
		if (this.getName() == null || this.getName().length() < 0) {
			return false;
		}
		return true;
	}

}
