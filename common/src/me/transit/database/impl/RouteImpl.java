package me.transit.database.impl;

import java.util.ArrayList;
import java.util.List;

import me.transit.database.Route;
import me.transit.database.RouteDocument;
import me.transit.database.Trip;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Route")
public class RouteImpl extends TransitDateImpl implements Route {
		
	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamAlias(Route.SHORTNAME)
	private String shortName = "";
	@XStreamAlias(Route.LONGNAME)
	private String longName = "";
	@XStreamAlias(Route.DESC)
	private String desc = "";
	@XStreamAlias(Route.TYPE)
	private RouteType type = RouteType.UNKOWN;
	@XStreamAlias("url")
	private String url = "";
	@XStreamAlias("color")
	private String color = "";
	@XStreamAlias("textColor")
	private String textColor = "";
	@XStreamImplicit(itemFieldName=Route.TRIPLIST)
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
	 * @return the textColor
	 */
	public List<Trip> getTripList()
	{
		return this.trips;
	}

	/**
	 * @param textColor the textColor to set
	 */
	public void setTripList(List<Trip> list) {
		this.trips = list;
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
		return builder.toString();
	}
		
	/**
	 * 
	 */
	public boolean valid() 
	{
		return true;
	}
	
	/**
	 * 
	 */
	public RouteDocument toRouteDocument()  {
		RouteDocument rtn = new RouteDocument();
		
		rtn.setAgency(this.getAgency().getName());
		rtn.setDesc(this.getDesc());
		rtn.setLongName(this.getLongName());
		rtn.setShortName(this.getShortName());
		rtn.setType(this.getType());
		rtn.setUUID(this.getUUID());
		
		return rtn;
	}
	
}
