package me.transit.database;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.database.CSVFieldType;
import me.database.mongo.AbstractDocument;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;

@GTFSFileModel(filename="stop_times.txt")
public class StopTime extends AbstractDocument implements CSVFieldType  {

	public static final String STOPID = "stopId";
	public static final String STOPNAME = "stopName";
	public static final String STOPHEADSIGN = "stopHeadSign";
	public static final String ARRIVALTIME = "arrivalTime";
	public static final String DROPOFFTYPE = "dropOffType";
	public static final String PICKUPTYPE = "pickupType";
	public static final String LOCATION = "location";

	public enum PickupType { REGULAR, NOPICKUP, PHONE, COORDINATE, UNKNOWN };
	
	private static final String SEPERATOR = ";";
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private String stopId = null;
	private List<Long> arrivalTime = new ArrayList<Long>();
	private String stopHeadSign = "";
	private String stopName = "";
	private PickupType pickupType = PickupType.REGULAR;
	private PickupType dropOffType = PickupType.REGULAR;
	private String tripId = null;
	
	private Double[] location = null;
	
	public StopTime()
	{
	}
	
	public StopTime(StopTime copy) 
	{
		if ( copy != null ) {
			setArrivalTime(copy.getArrivalTime());
			setDropOffType(copy.getDropOffType());
			setPickupType(copy.getPickupType());
			setStopHeadSign(copy.getStopHeadSign());
		}
	}
	
	/**
	 * @return the TripId
	 */
	@JsonGetter("trip_id")
	public String getTripId()
	{
		return tripId;
	}

	/**
	 * @param arrivalTime the TripId to set
	 */
	@GTFSSetter(column="trip_id")
	@JsonSetter("trip_id")
	public void setTripId(String id)
	{
		tripId = id;
	}
	
	/**
	 * @return the arrivalTime
	 */
	@JsonGetter("arrival_time")
	public List<Long> getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param arrivalTime the arrivalTime to set
	 */
	@GTFSSetter(column="arrival_time")
	@JsonSetter("arrival_time")
	public void setArrivalTime(List<Long> arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	/**
	 * 
	 * @param departureTime
	 */
	
	public void addArrivalTime(long arrivalTime) {
		this.arrivalTime.add(arrivalTime);
	}

	/**
	 * @return the stopId
	 */
	@JsonGetter("stop_id")
	public String getStopId() {
		return stopId;
	}

	/**
	 * @param stopId the stopId to set
	 */
	@GTFSSetter(column="stop_id")
	@JsonSetter("stop_id")
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	
	/**
	 * @return the stopId
	 */
	@JsonGetter("location")
	public Double[] getLocation() {
		return this.location;
	}
	
	/**
	 * @param stopId the stopId to set
	 */
	@GTFSSetter(column="location")
	@JsonSetter("location")
	public void setLocation(Double[] stopId) {
		this.location = stopId;
	}
	
	/**
	 * @return the stopId
	 */
	@JsonGetter("stop_name")
	public String getStopName() {
		return this.stopName;
	}

	/**
	 * @param stopId the stopId to set
	 */
	@GTFSSetter(column="stop_name")
	@JsonSetter("stop_name")
	public void setStopName(String name)
	{
		this.stopName = name;
	}

	/**
	 * @return the stopHeadSign
	 */
	@JsonGetter("stop_head_sign")
	public String getStopHeadSign() {
		return stopHeadSign;
	}

	/**
	 * @param stopHeadSign the stopHeadSign to set
	 */
	@GTFSSetter(column="stop_head_sign")
	@JsonSetter("stop_head_sign")
	public void setStopHeadSign(String stopHeadSign) {
		this.stopHeadSign = stopHeadSign;
	}

	/**
	 * @return the pickupType
	 */
	@JsonGetter("pickup_type")
	public PickupType getPickupType() {
		return pickupType;
	}

	/**
	 * @param pickupType the pickupType to set
	 */
	@GTFSSetter(column="pickup_type")
	@JsonSetter("pickup_type")
	public void setPickupType(PickupType pickupType) {
		this.pickupType = pickupType;
	}

	/**
	 * @return the dropOffType
	 */
	@JsonGetter("drop_off_type")
	public PickupType getDropOffType() {
		return dropOffType;
	}

	/**
	 * @param dropOffType the dropOffType to set
	 */
	@GTFSSetter(column="drop_off_type")
	@JsonSetter("drop_off_type")
	public void setDropOffType(PickupType dropOffType) {
		this.dropOffType = dropOffType;
	}

	/**
	 * 
	 */
	@Override
	public void fromCSVLine(String line) {
		
		String data[] = line.split(CSVFieldType.COMMA);
		
		String time[] = data[0].split(StopTime.SEPERATOR);
		this.getArrivalTime().clear();
		for ( String ndx : time) {
			this.addArrivalTime( Long.parseLong(ndx));
		}
				
		this.setStopHeadSign( data[1]);
		this.setDropOffType( PickupType.valueOf(data[2]));
		this.setPickupType(PickupType.valueOf(data[3]));
		this.setStopId( data[4]);
		this.setStopName( data[5]);
		this.setTripId(data[6]);
		
		return;
	}

	/**
	 * 
	 */
	@Override
	public String toCSVLine() {
		StringBuilder builder = new StringBuilder();
	
		StringBuilder tmp = new StringBuilder();
		for ( Long l : this.getArrivalTime()) {
			if ( tmp.length() > 0 ) { tmp.append(StopTime.SEPERATOR); }
			tmp.append(l.longValue());
		}
		builder.append(tmp);
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getStopHeadSign());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getDropOffType().name());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getPickupType().name());
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getStopId());
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getStopName());
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getTripId());
		
		return builder.toString();
	}
				
}
