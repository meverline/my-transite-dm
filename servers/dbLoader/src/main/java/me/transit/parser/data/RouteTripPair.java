package me.transit.parser.data;

import me.transit.database.Trip;

public class RouteTripPair {

	private final String routeId;
	private final Trip trip;

	/**
	 * 
	 * @param routeId
	 * @param trip
	 */
	public RouteTripPair(String routeId, Trip trip) {
		this.routeId = routeId;
		this.trip = trip;
	}

	/**
	 * 
	 * @return
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * 
	 * @return
	 */
	public Trip getTrip() {
		return trip;
	}
}
