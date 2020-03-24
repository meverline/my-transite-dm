package me.transit.database;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.database.mongo.IDocument;
import me.database.neo4j.AbstractGraphNode;
import me.database.neo4j.FIELD;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.json.AgencyToString;
import me.transit.json.Base64StringToGeometry;
import me.transit.json.GeometryToBase64String;

@Entity
@Table(name="tran_trip")
@DiscriminatorColumn(name = "trip_type")
@DiscriminatorValue("Trip")
@GTFSFileModel(filename="trips.txt")
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Trip extends AbstractGraphNode implements TransitData, IDocument  {
	
	public static final String ID = "id";
	public static final String SERVICE = "service";
	public static final String HEADSIGN = "headSign";
	public static final String SHORTNAME = "shortName";
	public static final String DIRECTIONID = "directionId";
	public static final String STOPTIMES = "stopTimes";

	public enum DirectionType { OUT_BOUND, IN_BOUND, UNKNOWN}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TRIP_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	@Column(name = "ID", nullable = false)
	private String id = null;

	@Column(name = "VERSION")
	private String version = "0.5";
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="SERVICE_DATE_UUID", nullable=false, updatable=false)
	private ServiceDate service = null;
	
	@Column(name="HEAD_SIGN")  
	private String headSign = "";
	
	@Column(name="NAME")  
	private String shortName = "";

	@Column(name="TRIP_NDX")
	private int routeTripIndex;
	
	@Column(name="DIRECTION")
	@Enumerated(EnumType.STRING) 
	private DirectionType directionId = DirectionType.UNKNOWN;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ROUTE_GEOMETRY_UUID", nullable=false, updatable=false)
	private RouteGeometry shape = null;
	
	private transient List<StopTime> stopTimes = new ArrayList<>();
	private transient String docId = null;
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#getUUID()
	 */
	@JsonGetter("_id")
	public String getDocId() {
		return docId;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setUUID(long)
	 */
	@JsonSetter("_id")
	public void setDocId(String uuid) {
		this.docId = uuid;
	}
	
	@Override
	@JsonGetter("@class")
	public String getDocClass() {
		return this.getClass().getName();
	}
	
	@Override
	@JsonSetter("@class")
	public void setDocClass() {
		
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#getUUID()
	 */
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setUUID(long)
	 */
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the agency
	 */
	@JsonGetter("agency_name")
	@JsonSerialize(converter = AgencyToString.class)
	public Agency getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	@JsonSetter("agency_name")
	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#getId()
	 */
	@JsonGetter("trip_id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setId(java.lang.String)
	 */
	@GTFSSetter(column="id")
	@JsonSetter("trip_id")
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#getVersion()
	 */
	@JsonGetter("version")
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setVersion(java.lang.String)
	 */
	@GTFSSetter(column="version")
	@JsonSetter("version")
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the service
	 */
	@JsonGetter("service_id")
	public ServiceDate getService() {
		return service;
	}
	
	/**
	 * @param service the service to set
	 */
	@GTFSSetter(column="service_id")
	@JsonSetter("service_id")
	public void setService(ServiceDate service) {
		this.service = service;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#getHeadSign()
	 */
	@JsonGetter("trip_headsign")
	public String getHeadSign() {
		return headSign;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setHeadSign(java.lang.String)
	 */
	@GTFSSetter(column="trip_headsign")
	@JsonSetter("trip_headsign")
	public void setHeadSign(String headSign) {
		this.headSign = headSign;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#getShortName()
	 */
	@JsonGetter("trip_shortname")
	public String getShortName() {
		return shortName;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setShortName(java.lang.String)
	 */
	@GTFSSetter(column="trip_shortname")
	@JsonSetter("trip_shortname")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#getDirectionId()
	 */
	@JsonGetter("direction_id")
	public DirectionType getDirectionId() {
		return directionId;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setDirectionId(me.transit.database.impl.TripImpl.DirectionType)
	 */
	@GTFSSetter(column="direction_id")
	@JsonSetter("direction_id")
	public void setDirectionId(DirectionType directionId) {
		this.directionId = directionId;
	}
	
	/**
	 * @return the shape
	 */
	@JsonSetter("shape_id")
	public RouteGeometry getShape() {
		return shape;
	}
	
	/**
	 * @param shape the shape to set
	 */
	@GTFSSetter(column="shape_id")
	@JsonSetter("shape_id")
	public void setShape(RouteGeometry shape) {
		this.shape = shape;
	}
	
	/**
	 * @return the stopTimes
	 */
	@JsonGetter("stop_times")
	public List<StopTime> getStopTimes() {
		return stopTimes;
	}

	/**
	 * @param stopTimes the stopTimes to set
	 */
	@JsonSetter("stop_times")
	public void setStopTimes(List<StopTime> stopTimes) {
		this.stopTimes = stopTimes;
	}

	@JsonGetter("trip_index")
	public int getRouteTripIndex() {
		return routeTripIndex;
	}

	@JsonSetter("trip_index")
	public void setRouteTripIndex(int routeTripIndex) {
		this.routeTripIndex = routeTripIndex;
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
	
	/*
	 * (non-Javadoc)
	 * @see me.transit.database.impl.TransitDateImpl#toString()
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#toString()
	 */

	@Override
	public String toString() {
		return "Trip{" +
				"uuid=" + uuid +
				", agency=" + agency +
				", id='" + id + '\'' +
				", version='" + version + '\'' +
				", service=" + service +
				", headSign='" + headSign + '\'' +
				", shortName='" + shortName + '\'' +
				", routeTripIndex=" + routeTripIndex +
				", directionId=" + directionId +
				", shape=" + shape +
				", stopTimes=" + stopTimes +
				", docId='" + docId + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Trip trip = (Trip) o;
		return uuid == trip.uuid &&
				routeTripIndex == trip.routeTripIndex &&
				Objects.equals(agency, trip.agency) &&
				Objects.equals(id, trip.id) &&
				Objects.equals(version, trip.version) &&
				Objects.equals(service, trip.service) &&
				Objects.equals(headSign, trip.headSign) &&
				Objects.equals(shortName, trip.shortName) &&
				directionId == trip.directionId &&
				Objects.equals(shape, trip.shape) &&
				Objects.equals(stopTimes, trip.stopTimes) &&
				Objects.equals(docId, trip.docId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, agency, id, version, service, headSign, shortName, routeTripIndex, directionId, shape, stopTimes, docId);
	}

	/*
	 * (non-Javadoc)
	 * @see me.transit.database.TransitData#valid()
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#valid()
	 */

	public boolean valid() 
	{
		if ( this.getService() == null || this.getStopTimes().size() < 1 )
		{
			return false;
		}
		return true;
	}
	
    /*
     * (non-Javadoc)
     * @see me.database.neo4j.IGraphNode#getProperties()
     */
	@Override
	public Map<String, String> getProperties() {
		Map<String, String> node = new HashMap<>();
		node.put(FIELD.trip.name(), makeKey());
		node.put(FIELD.db_name.name(), this.getHeadSign());
		node.put(FIELD.className.name(), this.getClass().getSimpleName());
		node.put(FIELD.direction.name(), this.getDirectionId().name());
		node.put(FIELD.trip_headSign.name(), this.makeHeadSignKey());
		if (this.getShortName() != null) {
			node.put(FIELD.db_id.name(), this.getShortName());
		}
		
		return node;
	}
	
	public String makeHeadSignKey() {
		return getHeadSign() + "@" + getAgency().getName();
	}

}
