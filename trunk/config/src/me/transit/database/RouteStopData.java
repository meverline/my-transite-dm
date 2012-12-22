package me.transit.database;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import me.database.CSVFieldType;

@XStreamAlias("RouteStopData")
public interface RouteStopData extends CSVFieldType{
	
	/**
	 * @return the routeShortName
	 */
	public String getRouteShortName();

	/**
	 * @param routeShortName the routeShortName to set
	 */
	public void setRouteShortName(String routeShortName);

	/**
	 * @return the tripHeadSign
	 */
	public String getTripHeadSign();

	/**
	 * @param tripHeadSign the tripHeadSign to set
	 */
	public void setTripHeadSign(String tripHeadSign);

}
