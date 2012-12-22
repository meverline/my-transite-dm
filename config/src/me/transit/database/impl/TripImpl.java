package me.transit.database.impl;

import java.util.ArrayList;
import java.util.List;

import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.StopTime;
import me.transit.database.Trip;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class TripImpl extends TransitDateImpl implements Trip {

	@XStreamAlias("service")
	private ServiceDate service = null;
	@XStreamAlias("headSign")
	private String headSign = "";
	@XStreamAlias("shortName")
	private String shortName = "";
	@XStreamAlias("directionId")
	private DirectionType directionId = DirectionType.UNKOWN;
	
	@XStreamAlias("shapeId")
	@XStreamConverter(me.database.ShapeConverter.class)
	private RouteGeometry shape = null;
	@XStreamImplicit(itemFieldName="stopTimes")
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
}
