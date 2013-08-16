package me.transit.database.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.database.CSVFieldType;
import me.factory.DaoBeanFactory;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.hadoop.HadoopUtils;
import me.transit.dao.mongo.IDocument;
import me.transit.database.Agency;
import me.transit.database.StopTime;
import me.transit.database.TransitStop;

import org.apache.hadoop.io.Writable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class StopTimeImpl implements StopTime, Writable {

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
		
		this.setStopHeadSign( data[2]);
		this.setDropOffType( PickupType.valueOf(data[3]));
		this.setPickupType(PickupType.valueOf(data[4]));
		
		if ( data[5].trim().length() > 0) {
			this.setShapeDistTravel( Double.parseDouble(data[5]));
		}
		
		return;
	}

	/**
	 * 
	 */
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
	
	/**
	 * 
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		this.setShapeDistTravel(in.readDouble());
		this.setDropOffType( PickupType.values()[in.readInt()]);
		this.setPickupType( PickupType.values()[in.readInt()]);
		this.setStopHeadSign( HadoopUtils.readString(in));
		this.setStopId( HadoopUtils.readString(in));
		this.setTripId( HadoopUtils.readString(in));
	}

	/**
	 * 
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(this.getShapeDistTravel());
		out.writeInt(this.getDropOffType().ordinal());
		out.writeInt(this.getPickupType().ordinal());
		
		HadoopUtils.writeString(this.getStopHeadSign(), out);
		HadoopUtils.writeString(this.getStopId(), out);
		HadoopUtils.writeString(this.getTripId(), out);
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