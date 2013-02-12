package me.transit.database;

import me.database.CSVFieldType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("StopTime")
public interface StopTime extends CSVFieldType {
	

	public enum PickupType { REGULAR, NOPICKUP, PHONE, COORDINATE, UNKNOWN };
	
	/**
	 * @return the TripId
	 */
	public String getTripId();

	/**
	 * @param arrivalTime the TripId to set
	 */
	public void setTripId(String id);
	
	/**
	 * @return the arrivalTime
	 */
	public long getArrivalTime();

	/**
	 * @param arrivalTime the arrivalTime to set
	 */
	public void setArrivalTime(long arrivalTime);

	/**
	 * @return the departureTime
	 */
	public long getDepartureTime() ;

	/**
	 * @param departureTime the departureTime to set
	 */
	public void setDepartureTime(long departureTime);

	/**
	 * @return the stopId
	 */
	public TransitStop getStop(Agency agency);

	/**
	 * @return the stopId
	 */
	public String getStopId();

	/**
	 * @param stopId the stopId to set
	 */
	public void setStopId(String stopId);

	/**
	 * @return the stopHeadSign
	 */
	public String getStopHeadSign();

	/**
	 * @param stopHeadSign the stopHeadSign to set
	 */
	public void setStopHeadSign(String stopHeadSign);

	/**
	 * @return the pickupType
	 */
	public PickupType getPickupType();

	/**
	 * @param pickupType the pickupType to set
	 */
	public void setPickupType(PickupType pickupType);

	/**
	 * @return the dropOffType
	 */
	public PickupType getDropOffType();

	/**
	 * @param dropOffType the dropOffType to set
	 */
	public void setDropOffType(PickupType dropOffType);

	/**
	 * @return the shapeDistTravel
	 */
	public double getShapeDistTravel();

	/**
	 * @param shapeDistTravel the shapeDistTravel to set
	 */
	public void setShapeDistTravel(double shapeDistTravel);

}
