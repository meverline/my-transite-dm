package me.transit.database.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.transit.dao.mongo.IDocument;
import me.transit.database.Route;
import me.transit.database.Trip;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Route")
public class RouteImpl extends TransitDateImpl implements Route {
	
	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamAlias("shortName")
	private String shortName = "";
	@XStreamAlias("longName")
	private String longName = "";
	@XStreamAlias("desc")
	private String desc = "";
	@XStreamAlias("routeType")
	private RouteType type = RouteType.UNKOWN;
	@XStreamAlias("url")
	private String url = "";
	@XStreamAlias("color")
	private String color = "";
	@XStreamAlias("textColor")
	private String textColor = "";
	@XStreamImplicit(itemFieldName="RouteTrip")
	private List<Trip> trips = new ArrayList<Trip>();
	
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the longName
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * @param longName the longName to set
	 */
	public void setLongName(String longName) {
		this.longName = longName;
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
	 * @return the type
	 */
	public RouteType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RouteType type) {
		this.type = type;
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
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the textColor
	 */
	public String getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	/**
	 * @param schedule the schedule to set
	 */
	public void setTripList(List<Trip> schedule) {
		this.trips = schedule;
	}

	/**
	 * @return the schedule
	 */
	public List<Trip> getTripList() {
		return trips;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder( "Route: {" + super.toString() + "}");
		
		builder.append("shortName: " + this.getShortName());
		builder.append("\n");
		builder.append("longName: " + this.getLongName());
		builder.append("\n");
		builder.append("desc: " + this.getDesc());
		builder.append("\n");
		builder.append("type: " + this.getType());
		builder.append("\n");
		builder.append("url: " + this.getUrl());
		builder.append("\n");
		builder.append("color: " + this.getColor());
		builder.append("\n");
		builder.append("text Color: " + this.getTextColor());
		builder.append("\n");
		builder.append("trip size: " + this.getTripList().size());
		return builder.toString();
	}

	@Override
	public Map<String, Object> toDocument() {
		
		Map<String,Object> rtn = new HashMap<String,Object>();

		rtn.put(IDocument.CLASS, RouteImpl.class.getName());
		rtn.put(IDocument.ID, this.getShortName() + ": " + this.getAgency().getName());
		
		rtn.put("agency", this.getAgency().getName());
		rtn.put("uuid", this.getUUID());
		rtn.put("shortName", this.getShortName());
		rtn.put("longName", this.getLongName());
		rtn.put("desc", this.getDesc());
		rtn.put("routeType", this.getType().name());
		rtn.put("trips", this.getTripList());
		return rtn;
	}
	
	@Override
	public String getCollection() {
		return Route.COLLECTION;
	}


	@Override
	public void fromDocument(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}
	
}
