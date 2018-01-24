package me.transit.database;

import me.database.CSVFieldType;
import me.transit.database.RouteStopData;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("RouteStopData")
public class RouteStopData implements CSVFieldType {

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamAlias("routeShortName")
	private String routeShortName = null;
	@XStreamAlias("tripHeadSign")
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
	public String getRouteShortName() {
		return routeShortName;
	}

	/**
	 * @param routeShortName the routeShortName to set
	 */
	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}

	/**
	 * @return the tripHeadSign
	 */
	public String getTripHeadSign() {
		return tripHeadSign;
	}

	/**
	 * @param tripHeadSign the tripHeadSign to set
	 */
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
