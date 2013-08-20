package me.transit.database.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.database.CSVFieldType;
import me.factory.DaoBeanFactory;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.mongo.IDocument;
import me.transit.database.Agency;
import me.transit.database.StopTime;
import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class StopTimeImpl implements StopTime {
	
	private static final String SEPERATOR = ";";

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamOmitField
	private String stopId = null;
	@XStreamAlias("arrivalTime")
	private List<Long> arrivalTime = new ArrayList<Long>();
	@XStreamAlias("departureTime")
	private List<Long> departureTime = new ArrayList<Long>();
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
	public List<Long> getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param arrivalTime the arrivalTime to set
	 */
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
	 * @return the departureTime
	 */
	public List<Long> getDepartureTime() {
		return departureTime;
	}

	/**
	 * @param departureTime the departureTime to set
	 */
	public void setDepartureTime(List<Long> departureTime) {
		this.departureTime = departureTime;
	}
	
	/**
	 * 
	 * @param departureTime
	 */
	public void addDepartureTime(long departureTime)
	{
		this.departureTime.add(departureTime);
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

	/**
	 * 
	 */
	@Override
	public void fromCSVLine(String line) {
		
		String data[] = line.split(CSVFieldType.COMMA);
		
		String time[] = data[0].split(StopTimeImpl.SEPERATOR);
		this.getArrivalTime().clear();
		for ( String ndx : time) {
			this.addArrivalTime( Long.parseLong(ndx));
		}
		
	    time = data[1].split(StopTimeImpl.SEPERATOR);
		this.getDepartureTime().clear();
		for ( String ndx : time) {
			this.addDepartureTime( Long.parseLong(ndx));
		}
		
		this.setStopHeadSign( data[2]);
		this.setDropOffType( PickupType.valueOf(data[3]));
		this.setPickupType(PickupType.valueOf(data[4]));
		
		if ( data[5].trim().length() > 0) {
			this.setShapeDistTravel( Double.parseDouble(data[5]));
		}
		this.setStopId( data[6]);
		this.setTripId(data[7]);
		
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
			if ( tmp.length() > 0 ) { tmp.append(StopTimeImpl.SEPERATOR); }
			tmp.append(l.longValue());
		}
		builder.append(tmp);
		builder.append( CSVFieldType.COMMA );
		
		tmp = new StringBuilder();
		for ( Long l : this.getDepartureTime()) {
			if ( tmp.length() > 0 ) { tmp.append(StopTimeImpl.SEPERATOR); }
			tmp.append(l .longValue());
		}
		builder.append(tmp);
	
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getStopHeadSign());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getDropOffType().name());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getPickupType().name());
		builder.append( CSVFieldType.COMMA);
		builder.append( this.getShapeDistTravel());
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getStopId());
		builder.append( CSVFieldType.COMMA );
		builder.append( this.getTripId());
		
		return builder.toString();
	}
	
	@Override
	public Map<String, Object> toDocument() {
		Map<String,Object> rtn = new HashMap<String,Object>();

		rtn.put(IDocument.CLASS, StopTimeImpl.class);
		if ( this.getStopId() != null ) {
			rtn.put("stopId", this.getStopId());
		}
		if ( this.getStopHeadSign() != null ) {
			rtn.put("headSign", this.getStopHeadSign());
		}
		
		if ( this.getShapeDistTravel() != -1 ) {
			rtn.put("shapeDistTravel", this.getShapeDistTravel());
		}
		
		Collections.sort(this.getDepartureTime());
		rtn.put("departureTime", this.getDepartureTime());
		Collections.sort(this.getArrivalTime());
		rtn.put("arrivalTime", this.getArrivalTime());
		rtn.put("dropOffType", this.getDropOffType().name());
		rtn.put("pickupType", this.getPickupType().name());
		return rtn;
	}
	
	@Override
	public String getCollection() {
		return StopTime.COLLECTION;
	}
	
}
