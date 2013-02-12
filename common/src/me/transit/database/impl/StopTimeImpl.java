package me.transit.database.impl;

import me.database.CSVFieldType;
import me.factory.DaoBeanFactory;
import me.transit.dao.TransiteStopDao;
import me.transit.database.Agency;
import me.transit.database.StopTime;
import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class StopTimeImpl implements StopTime {

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamOmitField
	private String stopId = null;
	@XStreamAlias("arrivalTime")
	private long arrivalTime = 0;
	@XStreamAlias("departureTime")
	private long departureTime = 0;
	@XStreamAlias("stopHeadSign")
	private String stopHeadSign = "";
	@XStreamAlias("pickupType")
	private PickupType pickupType = PickupType.UNKNOWN;
	@XStreamAlias("dropOffType")
	private PickupType dropOffType = PickupType.UNKNOWN;
	@XStreamAlias("shapeDistTravel")
	private double shapeDistTravel = -1;
	@XStreamOmitField
	private String tripId = null;
	
	public StopTimeImpl()
	{
	}
	
	public StopTimeImpl(StopTime copy) 
	{
		if ( copy != null ) {
			setArrivalTime(copy.getArrivalTime());
			setDepartureTime(copy.getDepartureTime());
			setDropOffType(copy.getDropOffType());
			setPickupType(copy.getPickupType());
			setStopHeadSign(copy.getStopHeadSign());
		}
	}
	
	/**
	 * @return the TripId
	 */
	public String getTripId()
	{
		return tripId;
	}

	/**
	 * @param arrivalTime the TripId to set
	 */
	public void setTripId(String id)
	{
		tripId = id;
	}
	
	/**
	 * @return the arrivalTime
	 */
	public long getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param arrivalTime the arrivalTime to set
	 */
	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
	 * @return the departureTime
	 */
	public long getDepartureTime() {
		return departureTime;
	}

	/**
	 * @param departureTime the departureTime to set
	 */
	public void setDepartureTime(long departureTime) {
		this.departureTime = departureTime;
	}

	/**
	 * @return the stopId
	 */
	public TransitStop getStop(Agency agency) {
		TransiteStopDao dao = 
			TransiteStopDao.class.cast(DaoBeanFactory.create().getDaoBean(TransiteStopDao.class));
		return TransitStop.class.cast(dao.loadById(getStopId(), agency.getName()));
	}

	/**
	 * @return the stopId
	 */
	public String getStopId() {
		return stopId;
	}

	/**
	 * @param stopId the stopId to set
	 */
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	/**
	 * @return the stopHeadSign
	 */
	public String getStopHeadSign() {
		return stopHeadSign;
	}

	/**
	 * @param stopHeadSign the stopHeadSign to set
	 */
	public void setStopHeadSign(String stopHeadSign) {
		this.stopHeadSign = stopHeadSign;
	}

	/**
	 * @return the pickupType
	 */
	public PickupType getPickupType() {
		return pickupType;
	}

	/**
	 * @param pickupType the pickupType to set
	 */
	public void setPickupType(PickupType pickupType) {
		this.pickupType = pickupType;
	}

	/**
	 * @return the dropOffType
	 */
	public PickupType getDropOffType() {
		return dropOffType;
	}

	/**
	 * @param dropOffType the dropOffType to set
	 */
	public void setDropOffType(PickupType dropOffType) {
		this.dropOffType = dropOffType;
	}

	/**
	 * @return the shapeDistTravel
	 */
	public double getShapeDistTravel() {
		return shapeDistTravel;
	}

	/**
	 * @param shapeDistTravel the shapeDistTravel to set
	 */
	public void setShapeDistTravel(double shapeDistTravel) {
		this.shapeDistTravel = shapeDistTravel;
	}

	@Override
	public void fromCSVLine(String line) {
		
		String data[] = line.split(CSVFieldType.COMMA);
		
		this.setArrivalTime( Long.parseLong(data[0]));
		this.setDepartureTime(Long.parseLong(data[1]));
		this.setStopHeadSign( data[2]);
		this.setDropOffType( PickupType.valueOf(data[3]));
		this.setPickupType(PickupType.valueOf(data[4]));
		
		if ( data[5].trim().length() > 0) {
			this.setShapeDistTravel( Double.parseDouble(data[5]));
		}
		
		return;
	}

	@Override
	public String toCSVLine() {
		StringBuilder builder = new StringBuilder();
	
		builder.append( this.getArrivalTime());
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getDepartureTime());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getStopHeadSign());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getDropOffType());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getPickupType());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getShapeDistTravel());
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getStopId());
		
		return builder.toString();
	}
}
