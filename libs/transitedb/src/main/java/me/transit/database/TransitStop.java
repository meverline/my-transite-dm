package me.transit.database;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import me.datamining.metric.IDataProvider;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.json.Base64StringToGeometry;
import me.transit.json.GeometryToBase64String;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tran_stop")
@DiscriminatorColumn(name = "tran_stop_type")
@DiscriminatorValue("TransitStop")
@GTFSFileModel(filename="stops.txt")
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class TransitStop extends TransitData implements IDataProvider  {
	
	public static final String STOP_NAME = "name";
	public static final String LOCATION = "location";

	public enum LocationType { STOP, STATION, ENTRENCE_EXIT, GENERIC_NODE, BOARDING_AREA, UNKOWN }
	public enum WheelChariBoardingType { EMPTY, SOME, NOT_AVALIABLE }

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STOP_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;

	@Column(name = "CODE")
	private String code = "";

	@Column(name = "NAME", nullable = false)
	private String name = null;

	@Column(name = "DESCRIPTION")
	private String desc = "";

	@Column(name = "LOCATION", columnDefinition = "Geometry")
	@Type(type="jts_geometry")
	private Point location = null;

	@Column(name = "ZONE")
	private String zoneId = null;

	@Column(name = "URL")
	private String url = "";

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private LocationType locationType = LocationType.UNKOWN;

	@Column(name = "PARENT_STATION")
	private int parentStation = -1;

	@Column(name = "WHEELCHAIR_BOARDING")
	@Enumerated(EnumType.STRING)
	private WheelChariBoardingType wheelchairBoarding = WheelChariBoardingType.EMPTY;

	@Column(name = "ID", nullable = false)
	private String id = null;

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getUUID()
	 */
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setUUID(long)
	 */
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getCode()
	 */
	@JsonGetter("code")
	public String getCode() {
		return code;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setCode(java.lang.String)
	 */
	@GTFSSetter(column="stop_code")
	@JsonSetter("code")
	public void setCode(String code) {
		this.code = code;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getName()
	 */
	@JsonGetter("name")
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setName(java.lang.String)
	 */
	@GTFSSetter(column="stop_name")
	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getDesc()
	 */
	@JsonGetter("desc")
	public String getDesc() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setDesc(java.lang.String)
	 */
	@GTFSSetter(column="stop_desc")
	@JsonSetter("desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getLocation()
	 */
	@JsonGetter("location")
	@JsonSerialize(converter = GeometryToBase64String.class)
	public Point getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setLocation(org.locationtech.jts.geom.Point)
	 */
	@GTFSSetter(column="location")
	@JsonSetter("location")
	@JsonDeserialize(converter = Base64StringToGeometry.class)
	public void setLocation(Point location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getZoneId()
	 */
	@JsonGetter("zone_id")
	public String getZoneId() {
		return zoneId;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setZoneId(java.lang.String)
	 */
	@GTFSSetter(column="zone_id")
	@JsonSetter("zone_id")
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getUrl()
	 */
	@JsonGetter("url")
	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setUrl(java.lang.String)
	 */
	@GTFSSetter(column="stop_url")
	@JsonSetter("url")
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getLocationType()
	 */
	@JsonGetter("location_type")
	public LocationType getLocationType() {
		return locationType;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setLocationType(me.transit.database.impl.TransitStopImpl.LocationType)
	 */
	@GTFSSetter(column="location_type")
	@JsonSetter("location_type")
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getParentStation()
	 */
	@JsonGetter("parent_station")
	public int getParentStation() {
		return parentStation;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setParentStation(int)
	 */
	@GTFSSetter(column="parent_station")
	@JsonSetter("parent_station")
	public void setParentStation(int parentStation) {
		this.parentStation = parentStation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.impl.TransitDateImpl#toString()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.database.TransitStop#setStopTimezone(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setStopTimezone(java.lang.String)
	 */
	@GTFSSetter(column="stop_timezone")
	@JsonSetter("stop_timezone")
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
	@JsonGetter("stop_timezone")
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
	@JsonGetter("wheelchair_boarding")
	public WheelChariBoardingType getWheelchairBoarding() {
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
	@GTFSSetter(column="wheelchair_boarding")
	@JsonSetter("wheelchair_boarding")
	public void setWheelchairBoarding(WheelChariBoardingType value) {
		wheelchairBoarding = value;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#getId()
	 */
	@JsonGetter("id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.TransitStop#setId(java.lang.String)
	 */
	@GTFSSetter(column="stop_id")
	@JsonSetter("id")
	public void setId(String id) {
		this.id = id;
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
		if (this.getName() == null || this.getName().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransitStop that = (TransitStop) o;
		return uuid == that.uuid &&
				parentStation == that.parentStation &&
				Objects.equals(this.getAgency(), that.getAgency()) &&
				Objects.equals(this.getId(), that.getId()) &&
				Objects.equals(this.getVersion(), that.getVersion()) &&
				Objects.equals(code, that.code) &&
				Objects.equals(name, that.name) &&
				Objects.equals(desc, that.desc) &&
				Objects.equals(location, that.location) &&
				Objects.equals(zoneId, that.zoneId) &&
				Objects.equals(url, that.url) &&
				locationType == that.locationType &&
				wheelchairBoarding == that.wheelchairBoarding;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, this.getAgency(), this.getId(), this.getVersion(), code, name, desc, location, zoneId, url, locationType, parentStation, wheelchairBoarding);
	}

	@Override
	public String toString() {
		return "TransitStop{" +
				"uuid=" + uuid +
				", agency=" + this.getAgency() +
				", id='" + this.getId() + '\'' +
				", version='" + this.getVersion() + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", location=" + location +
				", zoneId='" + zoneId + '\'' +
				", url='" + url + '\'' +
				", locationType=" + locationType +
				", parentStation=" + parentStation +
				", wheelchairBoarding=" + wheelchairBoarding +
				'}';
	}

}
