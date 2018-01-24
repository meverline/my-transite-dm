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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import me.database.mongo.IDocument;
import me.transit.database.Route;

@XStreamAlias("Route")
@Entity
@Table(name = "tran_route")
@DiscriminatorColumn(name = "route_type")
@DiscriminatorValue("TransitDateImpl")
public class Route implements TransitData {

	public final static String TRIPLIST = "tripList";
	public static final String SHORTNAME = "shortName";
	public static final String LONGNAME = "longName";
	public static final String DESC = "desc";
	public static final String TYPE = "type";
	
	public enum RouteType { TRAM, SUBWAY, RAIL, BUS, FERRY, CABLE_CAR, GONDOLA, FUNICULAR, UNKOWN };
	
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

	@Column(name = "SHORT_NAME", nullable = false)
	@XStreamAlias(Route.SHORTNAME)
	private String shortName = "";

	@Column(name = "LONG_NAME", nullable = false)
	@XStreamAlias(Route.LONGNAME)
	private String longName = "";

	@Column(name = "DESCRIPTION", columnDefinition = "text")
	@XStreamAlias(Route.DESC)
	private String desc = "";

	@Column(name = "TYPE", nullable = false)
	@XStreamAlias(Route.TYPE)
	private RouteType type = RouteType.UNKOWN;

	@Column(name = "URL")
	@XStreamAlias("url")
	private String url = "";

	@Column(name = "COLOR")
	@XStreamAlias("color")
	private String color = "";

	@Column(name = "TEXT_COLOR")
	@XStreamAlias("textColor")
	private String textColor = "";

	@OneToMany(mappedBy = "Route", cascade={CascadeType.PERSIST})
	@OrderBy("TRIP_INDX")
	@XStreamImplicit(itemFieldName = Route.TRIPLIST)
	private List<Trip> trips = new ArrayList<Trip>();

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
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the longName
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * @param longName
	 *            the longName to set
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the type
	 */
	public RouteType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(RouteType type) {
		this.type = type;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the textColor
	 */
	public String getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor
	 *            the textColor to set
	 */
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	/**
	 * @return the textColor
	 */
	public List<Trip> getTripList() {
		return this.trips;
	}

	/**
	 * @param textColor
	 *            the textColor to set
	 */
	public void setTripList(List<Trip> list) {
		this.trips = list;
	}

	/*
	 * (non-Javadoc)
	 * @see me.transit.database.impl.TransitDateImpl#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Route: {" + super.toString() + "}");

		builder.append("shortName: " + this.getShortName());
		builder.append("\n");
		builder.append("longName: " + this.getLongName());
		builder.append("\n");
		builder.append("desc: " + this.getDesc());
		builder.append("\n");
		builder.append("type: " + this.getType());
		builder.append("\n");
		builder.append("url: " + this.getUrl());
		builder.append("\n");
		builder.append("color: " + this.getColor());
		builder.append("\n");
		builder.append("text Color: " + this.getTextColor());
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see me.transit.database.TransitData#valid()
	 */
	@Override
	public boolean valid() {
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> toDocument() {

		Map<String, Object> rtn = new HashMap<String, Object>();

		rtn.put(IDocument.CLASS, Route.class.getName());
		if (this.getShortName().isEmpty()) {
			rtn.put(IDocument.ID, this.getLongName() + ": " + this.getAgency().getName());
		} else {
			rtn.put(IDocument.ID, this.getShortName() + ": " + this.getAgency().getName());
		}

		rtn.put(Agency.AGENCY, this.getAgency().getName());
		rtn.put(Agency.UUID, this.getUUID());
		rtn.put(Route.SHORTNAME, this.getShortName());
		rtn.put(Route.LONGNAME, this.getLongName());
		rtn.put(Route.DESC, this.getDesc());
		rtn.put(Route.TYPE, this.getType().name());
		return rtn;
	}

	/**
	 * 
	 */
	public void handleEnum(String key, Object value) {
		if (key.equals(Route.TYPE)) {
			this.setType(Route.RouteType.valueOf(value.toString()));
		}
	}

}
