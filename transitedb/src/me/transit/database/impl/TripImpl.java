package me.transit.database.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.database.mongo.IDocument;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.StopTime;
import me.transit.database.Trip;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class TripImpl extends TransitDateImpl implements Trip {

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamAlias(Trip.SERVICE)
	private ServiceDate service = null;
	@XStreamAlias(Trip.HEADSIGN)
	private String headSign = "";
	@XStreamAlias(Trip.SHORTNAME)
	private String shortName = "";
	@XStreamAlias(Trip.DIRECTIONID)
	private DirectionType directionId = DirectionType.UNKOWN;
	@XStreamAlias("shapeId")
	@XStreamConverter(me.database.ShapeConverter.class)
	private RouteGeometry shape = null;
	@XStreamImplicit(itemFieldName=Trip.STOPTIMES)
	private List<StopTime> stopTimes = new ArrayList<StopTime>();
	
	/**
	 * @return the service
	 */
	public ServiceDate getService() {
		return service;
	}
	
	/**
	 * @param service the service to set
	 */
	public void setService(ServiceDate service) {
		this.service = service;
	}
	
	/**
	 * @return the headSign
	 */
	public String getHeadSign() {
		return headSign;
	}
	
	/**
	 * @param headSign the headSign to set
	 */
	public void setHeadSign(String headSign) {
		this.headSign = headSign;
	}
	
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/**
	 * @return the directionId
	 */
	public DirectionType getDirectionId() {
		return directionId;
	}
	
	/**
	 * @param directionId the directionId to set
	 */
	public void setDirectionId(DirectionType directionId) {
		this.directionId = directionId;
	}
	
	/**
	 * @return the shape
	 */
	public RouteGeometry getShape() {
		return shape;
	}
	
	/**
	 * @param shape the shape to set
	 */
	public void setShape(RouteGeometry shape) {
		this.shape = shape;
	}
	
	/**
	 * @return the stopTimes
	 */
	public List<StopTime> getStopTimes() {
		return stopTimes;
	}

	/**
	 * @param stopTimes the stopTimes to set
	 */
	public void setStopTimes(List<StopTime> stopTimes) {
		this.stopTimes = stopTimes;
	}

	/**
	 * 
	 * @param stopTime
	 */
	public void addStopTime(StopTime stopTime) 
	{
		if ( stopTime != null) {
		   this.getStopTimes().add(stopTime);
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public StopTime findStopTimeById(String id) {
		for ( StopTime item : this.getStopTimes() ) {
			if ( item.getStopId().compareTo(id) == 0 ) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder( "Trip: {" + super.toString() + "}");
		
		builder.append("service: " + this.getService());
		builder.append("\n");
		builder.append("headSign: " + this.getHeadSign());
		builder.append("\n");
		builder.append("shortName" + this.getShortName());
		builder.append("\n");
		builder.append("directionId: " + this.getDirectionId());
		builder.append("\n");
		builder.append("stopsTimes size: " + this.getStopTimes().size());
		return builder.toString();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		boolean rtn = false;
		
		if ( Trip.class.isAssignableFrom(obj.getClass()) ) {
			Trip rhs = Trip.class.cast(obj);
			rtn = true;
			if ( ! this.getAgency().equals(rhs.getAgency()) ) {
				rtn = false;
			}
			if ( this.getService() != null && (! this.getService().equals(rhs.getService()))) {
				rtn = false;
			}
			if ( this.getDirectionId() != rhs.getDirectionId()) {
				rtn = false;
			}
			if ( this.getShortName() != null && (! this.getShortName().equals(rhs.getShortName())) ) {
				rtn = false;
			}
			if ( this.getHeadSign() != null && (! this.getHeadSign().equals(rhs.getHeadSign())) ) {
				rtn = false;
			}
			
		}
		return rtn;
	}
	
	
	public boolean valid() 
	{
		if ( this.getService() == null || this.getStopTimes().size() < 1 )
		{
			return false;
		}
		return true;
	}
	
    /**
     * 
     */
    @Override
    public Map<String, Object> toDocument() {
            Map<String,Object> rtn = new HashMap<String,Object>();

            rtn.put(IDocument.CLASS, TripImpl.class.getName());
            rtn.put(IDocument.ID, this.getUUID());
            rtn.put( Trip.ID, this.getId());
            if ( this.getService() != null ) {
                rtn.put( Trip.SERVICE, this.getService());
            }
            if ( this.getHeadSign() != null ) {
                    rtn.put( Trip.HEADSIGN, this.getHeadSign());
            }
            if ( this.getShortName() != null ) {
                    rtn.put( Trip.SHORTNAME, this.getShortName());
            }
            rtn.put( Trip.DIRECTIONID, this.getDirectionId().name());
            rtn.put( Trip.STOPTIMES, this.getStopTimes());
            return rtn;
    }
    
    @Override
    public void handleEnum(String key, Object value)
    {
            if ( key.equals( Trip.DIRECTIONID ) ) {
                    this.setDirectionId( Trip.DirectionType.valueOf(value.toString()));
            }
    }

}
