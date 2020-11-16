package me.transit.database;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import me.database.nsstore.IDocument;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="tran_trip")
@DiscriminatorColumn(name = "trip_type")
@DiscriminatorValue("Trip")
@GTFSFileModel(filename="trips.txt")
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonFilter("agencyFilter")
public class Trip implements TransitData, IDocument  {
	
	public static final String ID = "id";
	public static final String SERVICE = "service";
	public static final String STOPTIMES = "stopTimes";

	public enum DirectionType { OUT_BOUND, IN_BOUND, UNKNOWN}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TRIP_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;

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
		return Objects.hash(uuid, id, version, service, headSign, shortName, routeTripIndex, directionId, shape, stopTimes, docId);
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
		return this.getService() == null || this.getStopTimes().size() < 1;
	}

}
