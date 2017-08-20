package me.openMap.models.query.result;

import java.util.ArrayList;
import java.util.List;

import me.openMap.utils.RouteOverlay;
import me.transit.database.Agency;

public class RouteQueryResult {

	private String routeNumber = null;
	private String tripHeadSign = null;
	private Agency agency = null;
	private List<RouteOverlay> overlay = new ArrayList<RouteOverlay>();
	
	public RouteQueryResult(String route, String headSign, Agency agency) 
	{
		this.routeNumber = route;
		

		this.tripHeadSign = headSign;
		this.agency = agency;
	}
	
	/**
	 * @return the routeNumber
	 */
	public String getRouteNumber() {
		return routeNumber;
	}
	/**
	 * @return the agency
	 */
	public Agency getAgency() {
		return agency;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append( routeNumber);
		builder.append(" - ");
		builder.append(agency.getName());
		return builder.toString();
	}

	/**
	 * @return the overlay
	 */
	public List<RouteOverlay> getOverlay() {
		return overlay;
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
	
}
