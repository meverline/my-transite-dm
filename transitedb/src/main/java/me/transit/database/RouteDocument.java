package me.transit.database;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.transit.annotation.GTFSSetter;
import me.transit.database.Route.RouteType;

public class RouteDocument {

	private long uuid = -1;
	private long routeId = -1;
	private String agency = "";
	private String shortName = "";
	private String longName = "";
	private String desc = "";
	private RouteType type = RouteType.UNKOWN;
	private List<Trip> trips = new ArrayList<Trip>();

	/**
	 * @return the uuid
	 */
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the routeId
	 */
	@JsonGetter("route_id")
	public long getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId the routeId to set
	 */
	@JsonSetter("route_id")
	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return the agency
	 */
	@JsonGetter("agency")
	public String getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	@JsonSetter("agency")
	public void setAgency(String agency) {
		this.agency = agency;
	}

	/**
	 * @return the shortName
	 */
	@JsonGetter("short_name")
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	@GTFSSetter(column="short_name")
	@JsonSetter("short_name")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the longName
	 */
	@JsonGetter("long_name")
	public String getLongName() {
		return longName;
	}

	/**
	 * @param longName the longName to set
	 */
	@GTFSSetter(column="long_name")
	@JsonSetter("long_name")
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * @return the desc
	 */
	@JsonGetter("desc")
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	@GTFSSetter(column="desc")
	@JsonSetter("desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the type
	 */
	@JsonGetter("route_type")
	public RouteType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	@GTFSSetter(column="route_type")
	@JsonSetter("route_type")
	public void setType(RouteType type) {
		this.type = type;
	}

	/**
	 * @return the trips
	 */
	@JsonGetter("trips")
	public List<Trip> getTrips() {
		return trips;
	}

	/**
	 * @param trips the trips to set
	 */
	@JsonSetter("trips")
	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}
	
	
}
