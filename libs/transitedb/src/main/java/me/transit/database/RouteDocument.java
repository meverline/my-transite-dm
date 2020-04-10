package me.transit.database;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.database.mongo.AbstractDocument;
import me.transit.annotation.GTFSSetter;

public class RouteDocument extends AbstractDocument implements IRoute {

	private long id = 0;
	private long uuid = -1;
	private String routeId = "";
	private String agency = "";
	private String shortName = "";
	private String longName = "";
	private String desc = "";
	private RouteType type = RouteType.UNKOWN;
	private List<Trip> trips = new ArrayList<Trip>();
	
	public RouteDocument() {
		
	}
	
	public RouteDocument(Route route, List<Trip> trips) {
		setUUID(route.getUUID());
	    this.setRouteId(route.getId());
	    this.setAgency(route.getAgency().getName());
	    this.setShortName(route.getShortName());
	    this.setLongName(route.getLongName());
	    this.setType(route.getType());
	    this.getTrips().addAll(trips);
	}
	
	
	/**
	 * @param id the id to set
	 */
	@JsonGetter("id")
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	@JsonSetter("id")
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the uuid
	 */
	@Override
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	@Override
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the routeId
	 */
	@Override
	@JsonGetter("route_id")
	public String getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId the routeId to set
	 */
	@Override
	@JsonSetter("route_id")
	public void setRouteId(String routeId) {
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
	@Override
	@JsonGetter("short_name")
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	@Override
	@GTFSSetter(column="short_name")
	@JsonSetter("short_name")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the longName
	 */
	@Override
	@JsonGetter("long_name")
	public String getLongName() {
		return longName;
	}

	/**
	 * @param longName the longName to set
	 */
	@Override
	@GTFSSetter(column="long_name")
	@JsonSetter("long_name")
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * @return the desc
	 */
	@Override
	@JsonGetter("desc")
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	@Override
	@GTFSSetter(column="desc")
	@JsonSetter("desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the type
	 */
	@Override
	@JsonGetter("route_type")
	public RouteType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	@Override
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
