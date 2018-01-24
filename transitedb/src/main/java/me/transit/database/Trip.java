package me.transit.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import me.database.mongo.IDocument;
import me.transit.database.Trip;

@Entity
@Table(name="tran_trip")
@DiscriminatorColumn(name = "trip_type")
@DiscriminatorValue("TripImpl")
@XStreamAlias("Trip")
public class Trip implements TransitData, IDocument {
	
	public static final String ID = "id";
	public static final String SERVICE = "service";
	public static final String HEADSIGN = "headSign";
	public static final String SHORTNAME = "shortName";
	public static final String DIRECTIONID = "directionId";
	public static final String STOPTIMES = "stopTimes";

	public enum DirectionType { OUT_BOUND, IN_BOUND, UNKOWN };

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XStreamAlias("id")
	private long uuid = -1;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	@Column(name = "ID", nullable = false)
	@XStreamOmitField
	private String id = null;

	@Column(name = "VERSION")
	@XStreamAlias("version")
	private String version = "0.5";
	
	@ManyToOne()
	@JoinColumn(name="SERVICE_DATE_UUID", nullable=false, updatable=false)
	@XStreamAlias(Trip.SERVICE)
	private ServiceDate service = null;
	
	@Column(name="HEAD_SIGN")  
	@XStreamAlias(Trip.HEADSIGN)
	private String headSign = "";
	
	@Column(name="NAME")  
	@XStreamAlias(Trip.SHORTNAME)
	private String shortName = "";
	
	@Column(name="DIRECTION")
	@Enumerated(EnumType.STRING) 
	@XStreamAlias(Trip.DIRECTIONID)
	private DirectionType directionId = DirectionType.UNKOWN;
	
	@ManyToOne()
	@JoinColumn(name="ROUTE_GEOMETRY_UUID", nullable=false, updatable=false)
	@XStreamAlias("shapeId")
	@XStreamConverter(me.database.ShapeConverter.class)
	private RouteGeometry shape = null;
	
	@XStreamImplicit(itemFieldName=Trip.STOPTIMES)
	private List<StopTime> stopTimes = new ArrayList<StopTime>();
	
	/**
	 * @return the uuid
	 */
	public long getUUID() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the agency
	 */
	public Agency getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

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
	
	/*
	 * (non-Javadoc)
	 * @see me.transit.database.impl.TransitDateImpl#toString()
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
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see me.transit.database.TransitData#valid()
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
     * @see me.database.mongo.IDocument#toDocument()
     */
    @Override
    public Map<String, Object> toDocument() {
            Map<String,Object> rtn = new HashMap<String,Object>();

            rtn.put(IDocument.CLASS, Trip.class.getName());
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
    
    /*
     * (non-Javadoc)
     * @see me.database.mongo.IDocument#handleEnum(java.lang.String, java.lang.Object)
     */
    @Override
    public void handleEnum(String key, Object value)
    {
            if ( key.equals( Trip.DIRECTIONID ) ) {
                    this.setDirectionId( Trip.DirectionType.valueOf(value.toString()));
            }
    }

}
