package me.transit.database;

import java.util.List;

import me.transit.dao.mongo.IDocument;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Trip")
public interface Trip extends TransitData, IDocument {

	public static final String ID = "id";
	public static final String SERVICE = "service";
	public static final String HEADSIGN = "headSign";
	public static final String SHORTNAME = "shortName";
	public static final String DIRECTIONID = "directionId";
	public static final String STOPTIMES = "stopTimes";

	public enum DirectionType { OUT_BOUND, IN_BOUND, UNKOWN };
	
	/**
	 * @return the service
	 */
	public ServiceDate getService();
	
	/**
	 * @param service the service to set
	 */
	public void setService(ServiceDate service);
	
	/**
	 * @return the headSign
	 */
	public String getHeadSign();
	
	/**
	 * @param headSign the headSign to set
	 */
	public void setHeadSign(String headSign);
	
	/**
	 * @return the shortName
	 */
	public String getShortName();
	
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName);
	
	/**
	 * @return the directionId
	 */
	public DirectionType getDirectionId();
	
	/**
	 * @param directionId the directionId to set
	 */
	public void setDirectionId(DirectionType directionId);
	
	/**
	 * @return the shape
	 */
	public RouteGeometry getShape();
	
	/**
	 * @param shape the shape to set
	 */
	public void setShape(RouteGeometry shape);
	
	/**
	 * @return the stopTimes
	 */
	public List<StopTime> getStopTimes();

	/**
	 * @param stopTimes the stopTimes to set
	 */
	public void setStopTimes(List<StopTime> stopTimes);

	/**
	 * 
	 * @param stopTime
	 */
	public void addStopTime(StopTime stopTime);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public StopTime findStopTimeById(String id);
	
}
