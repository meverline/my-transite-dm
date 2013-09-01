package me.transit.database;

import java.util.List;

import me.transit.dao.mongo.IDocument;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Route")
public interface Route extends TransitData, IDocument {
	
	public final static String TRIPLIST = "tripList";
	public static final String SHORTNAME = "shortName";
	public static final String LONGNAME = "longName";
	public static final String DESC = "desc";
	public static final String TYPE = "type";
	
	
	public enum RouteType { TRAM, SUBWAY, RAIL, BUS, FERRY, CABLE_CAR, GONDOLA, FUNICULAR, UNKOWN };
	
	/**
	 * @return the shortName
	 */
	public String getShortName();

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName);

	/**
	 * @return the longName
	 */
	public String getLongName();

	/**
	 * @param longName the longName to set
	 */
	public void setLongName(String longName);

	/**
	 * @return the desc
	 */
	public String getDesc();

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc);

	/**
	 * @return the type
	 */
	public RouteType getType();

	/**
	 * @param type the type to set
	 */
	public void setType(RouteType type);

	/**
	 * @return the url
	 */
	public String getUrl();

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url);

	/**
	 * @return the color
	 */
	public String getColor();

	/**
	 * @param color the color to set
	 */
	public void setColor(String color);

	/**
	 * @return the textColor
	 */
	public String getTextColor();

	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(String textColor);
	
	/**
	 * @return the textColor
	 */
	public List<Trip> getTripList();

	/**
	 * @param textColor the textColor to set
	 */
	public void setTripList(List<Trip> list);
		
}
