package me.transit.database;

public class RouteTrip {
	
	private String routeId = null;
	private String tripId = null;
	
	/**
	 * 
	 */
	public RouteTrip() {
	}
	
	/**
	 * 
	 * @param routeId
	 * @param tripId
	 */
	public RouteTrip(String routeId, String tripId) {
		this.setRouteId(routeId);
		this.setTripId(tripId);
	}
	
	/**
	 * 
	 * @param in
	 */
	public RouteTrip(String in) {
		this.fromString(in);
	}

	/**
	 * @return the routeId
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId the routeId to set
	 */
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return the tripId
	 */
	public String getTripId() {
		return tripId;
	}

	/**
	 * @param tripId the tripId to set
	 */
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.getRouteId() + "," + this.getTripId();
	}
	
	/**
	 * 
	 * @param input
	 */
	public void fromString(String input) {
		String data[] = input.split(",");
		
		this.setRouteId(data[0]);
		this.setTripId(data[1]);
	}
	
	
}
