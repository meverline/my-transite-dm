package me.transit.database;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.database.CSVFieldType;
import me.transit.annotation.GTFSSetter;
import me.transit.database.RouteStopData;

public class RouteStopData implements CSVFieldType {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private String routeShortName = null;
	private String tripHeadSign = null;

	/**
	 * 
	 */
	public RouteStopData()
	{
	}
	
	/**
	 * 
	 * @param rshn
	 * @param ths
	 */
	public RouteStopData(String rshn, String ths)
	{
		this.setRouteShortName(rshn);
		this.setTripHeadSign(ths);
	}
	
	/**
	 * 
	 * @param copy
	 */
	public RouteStopData(RouteStopData copy) 
	{
		if ( copy != null ) {
			this.setRouteShortName(copy.getRouteShortName());
			this.setTripHeadSign(copy.getTripHeadSign());
		}
	}
	
	/**
	 * @return the routeShortName
	 */
	@JsonGetter("shortName")
	public String getRouteShortName() {
		return routeShortName;
	}

	/**
	 * @param routeShortName the routeShortName to set
	 */
	@GTFSSetter(column="shortName")
	@JsonSetter("shortName")
	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}

	/**
	 * @return the tripHeadSign
	 */
	@JsonGetter("headSign")
	public String getTripHeadSign() {
		return tripHeadSign;
	}

	/**
	 * @param tripHeadSign the tripHeadSign to set
	 */
	@GTFSSetter(column="headSign")
	@JsonSetter("headSign")
	public void setTripHeadSign(String tripHeadSign) {
		this.tripHeadSign = tripHeadSign;
	}

	@Override
	public String toCSVLine() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(this.getRouteShortName());
		builder.append(CSVFieldType.COMMA);
		builder.append(this.getTripHeadSign());
		return builder.toString();
	}

	@Override
	public void fromCSVLine(String line) {
		
		String data[] = line.split(CSVFieldType.COMMA);
		
		this.setRouteShortName(data[0]);
		this.setTripHeadSign(data[1]);
		return;
	}
	
}
