package me.transit.database.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.database.CSVFieldType;
import me.database.mongo.IDocument;
import me.factory.DaoBeanFactory;
import me.transit.dao.TransiteStopDao;
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
	@XStreamAlias(StopTime.ARRIVALTIME)
	private List<Long> arrivalTime = new ArrayList<Long>();
	@XStreamAlias(StopTime.STOPHEADSIGN)
	private String stopHeadSign = "";
	@XStreamAlias(StopTime.STOPNAME)
	private String stopName = "";
	@XStreamAlias(StopTime.PICKUPTYPE)
	private PickupType pickupType = PickupType.REGULAR;
	@XStreamAlias(StopTime.DROPOFFTYPE)
	private PickupType dropOffType = PickupType.REGULAR;
	@XStreamOmitField
	private String tripId = null;
	@XStreamOmitField
	private Double[] location = null;
	
	public StopTimeImpl()
	{
	}
	
	public StopTimeImpl(StopTime copy) 
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
	 * @return the stopId
	 */
	public Double[] getLocation() {
		return this.location;
	}
	
	/**
	 * @param stopId the stopId to set
	 */
	public void setLocation(Double[] stopId) {
		this.location = stopId;
	}
	
	/**
	 * @return the stopId
	 */
	public String getStopName() {
		return this.stopName;
	}

	/**
	 * @param stopId the stopId to set
	 */
	public void setStopName(String name)
	{
		this.stopName = name;
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
			if ( tmp.length() > 0 ) { tmp.append(StopTimeImpl.SEPERATOR); }
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
	
    @Override
    public Map<String, Object> toDocument() {
            Map<String,Object> rtn = new HashMap<String,Object>();

            rtn.put(IDocument.CLASS, StopTimeImpl.class.getName());
            if ( this.getStopId() != null ) {
                    rtn.put( StopTime.STOPID, this.getStopId());
            }
            if ( this.getStopName() != null ) {
                    rtn.put( StopTime.STOPNAME, this.getStopName());
            }
            if ( this.getStopHeadSign() != null ) {
                    rtn.put( StopTime.STOPHEADSIGN, this.getStopHeadSign());
            }
                            
            Collections.sort(this.getArrivalTime());
            rtn.put( StopTime.ARRIVALTIME, this.getArrivalTime());
            rtn.put( StopTime.DROPOFFTYPE, this.getDropOffType().name());
            rtn.put( StopTime.PICKUPTYPE, this.getPickupType().name());
            
            if ( location != null ) {
                rtn.put( StopTime.LOCATION, location);
            }
            return rtn;
    }
    
    /**
     * 
     */
    @Override
    public void handleEnum(String key, Object value)
    {
            if ( key.equals(StopTime.PICKUPTYPE) ) {
                    this.setPickupType( StopTime.PickupType.valueOf(value.toString()));
            } else  if ( key.equals(StopTime.DROPOFFTYPE) ) {
                    this.setDropOffType( StopTime.PickupType.valueOf(value.toString()));
            }
    }
				
}
