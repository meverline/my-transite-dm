package me.transit.database;

import java.util.HashMap;
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
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Point;

import me.database.neo4j.FIELD;
import me.database.neo4j.AbstractGraphNode;
import me.datamining.metric.IDataProvider;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;

@Entity
@Table(name = "tran_stop")
@Inheritance
@DiscriminatorColumn(name = "tran_stop_type")
@DiscriminatorValue("TransitStopImpl")
@XStreamAlias("TransitStop")
@GTFSFileModel(filename="stops.txt")
public class TransitStop extends AbstractGraphNode implements TransitData, IDataProvider  {
	
	public static final String STOP_NAME = "name";
	public static final String LOCATION = "location";

	public enum LocationType { STOP, STATION, UNKNOW };

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

	@Column(name = "CODE")
	@XStreamAlias("code")
	private String code = "";

	@Column(name = "NAME", nullable = false)
	@XStreamAlias("name")
	private String name = null;

	@Column(name = "DESCRIPTION")
	@XStreamAlias("desc")
	private String desc = "";

	@Column(name = "LOCATION", columnDefinition = "Geometry")
	@Type(type="jts_geometry")
	@XStreamAlias("location")
	@XStreamConverter(me.database.PointConverter.class)
	private Point location = null;

	@Column(name = "ZONE")
	@XStreamAlias("zoneId")
	private String zoneId = null;

	@Column(name = "URL")
	@XStreamAlias("url")
	private String url = "";

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	@XStreamAlias("locationType")
	private LocationType locationType = LocationType.UNKNOW;

	@Column(name = "PARENT_STATION")
	@XStreamAlias("parentStation")
	private int parentStation = -1;

	private boolean wheelchairBoarding;

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getUUID()
	 */

	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setUUID(long)
	 */

	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getAgency()
	 */

	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setAgency(me.transit.database.impl.Agency)
	 */

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getId()
	 */

	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setId(java.lang.String)
	 */
	@GTFSSetter(column="id")
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getVersion()
	 */

	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setVersion(java.lang.String)
	 */
	@GTFSSetter(column="version")
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getCode()
	 */

	public String getCode() {
		return code;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setCode(java.lang.String)
	 */
	@GTFSSetter(column="code")
	public void setCode(String code) {
		this.code = code;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getName()
	 */

	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setName(java.lang.String)
	 */
	@GTFSSetter(column="name")
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getDesc()
	 */


	public String getDesc() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setDesc(java.lang.String)
	 */
	@GTFSSetter(column="desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getLocation()
	 */

	public Point getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setLocation(com.vividsolutions.jts.geom.Point)
	 */
	@GTFSSetter(column="location")
	public void setLocation(Point location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getZoneId()
	 */

	public String getZoneId() {
		return zoneId;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setZoneId(java.lang.String)
	 */
	@GTFSSetter(column="zoneId")
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getUrl()
	 */

	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setUrl(java.lang.String)
	 */
	@GTFSSetter(column="url")
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getLocationType()
	 */

	public LocationType getLocationType() {
		return locationType;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setLocationType(me.transit.database.impl.TransitStopImpl.LocationType)
	 */
	@GTFSSetter(column="locationType")
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getParentStation()
	 */

	public int getParentStation() {
		return parentStation;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setParentStation(int)
	 */
	@GTFSSetter(column="parentStation")
	public void setParentStation(int parentStation) {
		this.parentStation = parentStation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.impl.TransitDateImpl#toString()
	 */

	public String toString() {
		StringBuilder builder = new StringBuilder("TransiteStop: {" + super.toString() + "}");

		builder.append("code: " + this.getCode());
		builder.append("\n");
		builder.append("name: " + this.getName());
		builder.append("\n");
		builder.append("desc: " + this.getCode());
		builder.append("\n");
		builder.append("location: " + this.getName());
		builder.append("\n");
		builder.append("ZoneId: " + this.getCode());
		builder.append("\n");
		builder.append("url: " + this.getName());
		builder.append("\n");
		builder.append("locationType: " + this.getLocationType());
		builder.append("\n");
		builder.append("parentStation: " + this.getParentStation());
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#setStopTimezone(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setStopTimezone(java.lang.String)
	 */
	@GTFSSetter(column="stopTimezone")
	public void setStopTimezone(String value) {
		this.getAgency().setTimezone(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#getStopTimezone()
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getStopTimezone()
	 */

	public String getStopTimezone() {
		return this.getAgency().getTimezone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#getWheelchairBoarding()
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getWheelchairBoarding()
	 */

	public boolean getWheelchairBoarding() {
		return wheelchairBoarding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#setWheelchairBoarding(boolean)
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setWheelchairBoarding(boolean)
	 */
	@GTFSSetter(column="wheelchairBoarding")
	public void setWheelchairBoarding(boolean value) {
		wheelchairBoarding = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitData#valid()
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#valid()
	 */

	public boolean valid() {
		if (this.getName() == null || this.getName().length() < 0) {
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
		
		node.put( FIELD.stop.name(), makeKey());
        node.put( FIELD.db_name.name(), this.getName());
        node.put( FIELD.db_id.name(), this.getId());
        node.put(FIELD.className.name(), this.getClass().getSimpleName());
        node.put(FIELD.coordinate.name(),this.makeCoordinateKey());
        return node;
	}
	
	public String makeCoordinateKey() {		
		StringBuffer key = new StringBuffer();
		key.append( getLocation().getX() );
		key.append(",");
		key.append( getLocation().getY() );
		return key.toString();
	}

}
