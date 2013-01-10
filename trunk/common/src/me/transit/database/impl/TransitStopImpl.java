package me.transit.database.impl;

import java.util.ArrayList;
import java.util.List;

import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Point;

public class TransitStopImpl extends TransitDateImpl implements TransitStop {

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamAlias("code")
	private long code = -1;
	@XStreamAlias("name")
	private String name = "";
	@XStreamAlias("desc")
	private String desc = "";
	@XStreamAlias("location")
	@XStreamConverter(me.database.PointConverter.class)
	private Point location = null;
	@XStreamAlias("zoneId")
	private String   zoneId = null;
	@XStreamAlias("url")
	private String   url = "";
	@XStreamAlias("locationType")
	private LocationType locationType = LocationType.UNKNOW;
	@XStreamAlias("parentStation")
	private int parentStation = -1;
	@XStreamImplicit(itemFieldName="TripRoutes")
	private List<RouteStopData> routes = new ArrayList<RouteStopData>();
		
	/**
	 * @return the code
	 */
	public long getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(long code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
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
	 * @param desc the desc to set
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
	 * @param location the location to set
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
	 * @param zoneId the zoneId to set
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
	 * @param url the url to set
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
	 * @param locationType the locationType to set
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
	 * @param parentStation the parentStation to set
	 */
	public void setParentStation(int parentStation) {
		this.parentStation = parentStation;
	}

	/**
	 * @return the routes
	 */
	public List<RouteStopData> getRoutes() {
		return routes;
	}

	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(List<RouteStopData> routes) {
		this.routes = routes;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder( "TransiteStop: {" + super.toString() + "}");
		
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
		builder.append("\n");
		builder.append("routes size: " + this.getRoutes().size());
		return builder.toString();
	}

}
