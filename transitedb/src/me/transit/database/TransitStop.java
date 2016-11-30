package me.transit.database;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.vividsolutions.jts.geom.Point;
import me.datamining.metric.IDataProvider;

@XStreamAlias("TransitStop")
public interface TransitStop extends TransitData, IDataProvider  {
	
	public static final String STOP_NAME = "name";
	public static final String LOCATION = "location";
	
	public enum LocationType { STOP, STATION, UNKNOW };
	
	/**
	 * @return the code
	 */
	public String getCode();

	/**
	 * @param code the code to set
	 */
	public void setCode(String code);

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * @return the desc
	 */
	public String getDesc();

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc);

	/**
	 * @return the location
	 */
	public Point getLocation();

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location);

	/**
	 * @return the zoneId
	 */
	public String getZoneId();

	/**
	 * @param zoneId the zoneId to set
	 */
	public void setZoneId(String zoneId);

	/**
	 * @return the url
	 */
	public String getUrl();

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url);
	
	/**
	 * @return the locationType
	 */
	public LocationType getLocationType();

	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(LocationType locationType);

	/**
	 * @return the parentStation
	 */
	public int getParentStation();
	
	/**
	 * @param parentStation the parentStation to set
	 */
	public void setParentStation(int parentStation);
	
	public void setStopTimezone(String value);
	public String getStopTimezone();
	
	public boolean getWheelchairBoarding();
	public void setWheelchairBoarding(boolean value);
	
}
