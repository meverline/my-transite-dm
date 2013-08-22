package me.transit.database;

import java.util.List;

import me.database.CSVFieldType;
import me.transit.dao.mongo.IDocument;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("StopTime")
public interface StopTime extends CSVFieldType, IDocument {
	
	public static final String STOPID = "stopId";
	public static final String STOPNAME = "stopName";
	public static final String HEADSIGN = "headSign";
	public static final String ARRIVALTIME = "arrivalTime";
	public static final String DROPOFFTYPE = "dropOffType";
	public static final String PICKUPTYPE = "pickupType";

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
	public List<Long> getArrivalTime();

	/**
	 * @param arrivalTime the arrivalTime to set
	 */
	public void setArrivalTime(List<Long> arrivalTime);
	
	/**
	 * 
	 * @param departureTime
	 */
	public void addArrivalTime(long arrivalTime);

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
	 * @return the stopId
	 */
	public String getStopName();

	/**
	 * @param stopId the stopId to set
	 */
	public void setStopName(String name);


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

}
