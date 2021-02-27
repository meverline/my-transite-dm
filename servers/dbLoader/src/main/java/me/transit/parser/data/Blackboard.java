package me.transit.parser.data;

import lombok.extern.apachecommons.CommonsLog;
import me.math.kdtree.MinBoundingRectangle;
import me.transit.database.Agency;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.Trip;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value="blackboard")
@CommonsLog
public class Blackboard {

	private final Map<String, String> routeShortName = new HashMap<>();
	private final Map<String, RouteGeometry> shaps = new HashMap<>();
	private final Map<String, ServiceDate> service = new HashMap<>();
	private final Map<String, RouteTripPair> tripMap = new HashMap<>();
	private final Map<String, List<Trip>> routeToTrips = new HashMap<>();
	private final Map<String, Long> routeuuid = new HashMap<>();
	private final Map<String, Long> stopuuid = new HashMap<>();
	private Agency agency = null;
	private MinBoundingRectangle mbr = null;

	/**
	 * 
	 */
	public void reset() {
		this.setAgency(null);
		this.resetMBR();
		this.getRouteShortName().clear();
		this.getShapes().clear();
		this.getService().clear();
		this.getTripMap().clear();
		this.getRouteToTrips().clear();
	}

	/**
	 * 
	 * @return
	 */
	public MinBoundingRectangle getMBR() {
		if (mbr == null) {
			mbr = new MinBoundingRectangle();
		}
		return this.mbr;
	}

	/**
	 * 
	 */
	public void resetMBR() {
		mbr = new MinBoundingRectangle();
	}

	/**
	 * @return the agencyId
	 */
	public String getAgencyName() {
		return agency.getName();
	}

	/**
	 * @param agencyId
	 *            the agencyId to set
	 */
	public void setAgency(Agency agencyId) {
		this.agency = agencyId;
	}

	/**
	 * @param agencyId
	 *            the agencyId to set
	 */
	public Agency getAgency() {
		return this.agency;
	}

	/**
	 * @return the routeShortName
	 */
	public Map<String, String> getRouteShortName() {
		return routeShortName;
	}

	/**
	 * @return the shaps
	 */
	public Map<String, RouteGeometry> getShapes() {
		return shaps;
	}

	/**
	 * @return the service
	 */
	public Map<String, ServiceDate> getService() {
		return service;
	}

	/**
	 * @return the tripMap
	 */
	public Map<String, RouteTripPair> getTripMap() {
		return tripMap;
	}

	/**
	 * @return the routeToTrips
	 */
	public Map<String, List<Trip>> getRouteToTrips() {
		return routeToTrips;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ServiceDate getServiceDate(long id) {
		if (this.service.containsKey(Long.toString(id))) {
			return this.service.get(Long.toString(id));
		}
		log.info(this.getClass().getSimpleName() + " ServiceData not found: " + id);
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public RouteGeometry getShape(long id) {
		if (this.shaps.containsKey(Long.toString(id))) {
			return this.shaps.get(Long.toString(id));
		}
		log.info(this.getClass().getSimpleName() + " Shape not found: " + id);
		return null;
	}

	/**
	 * @return the routeuuid
	 */
	public Map<String, Long> getRouteuuid() {
		return routeuuid;
	}

	public Map<String, Long> getStopuuid() { return stopuuid; }

}
