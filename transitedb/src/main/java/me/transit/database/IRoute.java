package me.transit.database;


public interface IRoute {

	public enum RouteType { TRAM, SUBWAY, RAIL, BUS, FERRY, CABLE_CAR, GONDOLA, FUNICULAR, UNKOWN };

	/**
	 * @return the uuid
	 */
	long getUUID();

	/**
	 * @param uuid the uuid to set
	 */
	void setUUID(long uuid);

	/**
	 * @return the routeId
	 */
	String getRouteId();

	/**
	 * @param routeId the routeId to set
	 */
	void setRouteId(String routeId);

	/**
	 * @return the shortName
	 */
	String getShortName();

	/**
	 * @param shortName the shortName to set
	 */
	void setShortName(String shortName);

	/**
	 * @return the longName
	 */
	String getLongName();

	/**
	 * @param longName the longName to set
	 */
	void setLongName(String longName);

	/**
	 * @return the desc
	 */
	String getDesc();

	/**
	 * @param desc the desc to set
	 */
	void setDesc(String desc);

	/**
	 * @return the type
	 */
	RouteType getType();

	/**
	 * @param type the type to set
	 */
	void setType(RouteType type);

}