package me.transit.database;

import java.util.ArrayList;
import java.util.List;

import me.transit.annotation.GTFSSetter;
import me.transit.database.Route.RouteType;

public class RouteDocument {

	private long uuid = -1;
	private String agency = "";
	private String shortName = "";
	private String longName = "";
	private String desc = "";
	private RouteType type = RouteType.UNKOWN;
	private List<Trip> trips = new ArrayList<Trip>();

	/**
	 * @return the uuid
	 */
	public long getUUID() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the agency
	 */
	public String getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	@GTFSSetter(column="shortName")
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
	@GTFSSetter(column="longName")
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
	@GTFSSetter(column="desc")
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
	@GTFSSetter(column="type")
	public void setType(RouteType type) {
		this.type = type;
	}

	/**
	 * @return the trips
	 */
	public List<Trip> getTrips() {
		return trips;
	}

	/**
	 * @param trips the trips to set
	 */
	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}
	
	
}
