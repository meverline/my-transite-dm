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
import javax.persistence.Inheritance;
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
import me.database.neo4j.FIELD;
import me.database.neo4j.AbstractGraphNode;

@XStreamAlias("Route")
@Entity
@Table(name = "tran_route")
@Inheritance
@DiscriminatorColumn(name = "route_type")
@DiscriminatorValue("TransitDateImpl")
public class Route extends AbstractGraphNode implements TransitData {
	
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

	@OneToMany(cascade={CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROUTE_UUID", nullable = false, updatable = false)
	@OrderBy("TRIP_INDX")
	@XStreamImplicit(itemFieldName = Route.TRIPLIST)
	private List<Trip> trips = new ArrayList<Trip>();

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getUUID()
	 */

	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setUUID(long)
	 */

	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getAgency()
	 */

	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setAgency(me.transit.database.impl.Agency)
	 */

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getId()
	 */

	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setId(java.lang.String)
	 */

	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getVersion()
	 */

	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setVersion(java.lang.String)
	 */

	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getShortName()
	 */

	public String getShortName() {
		return shortName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setShortName(java.lang.String)
	 */

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getLongName()
	 */

	public String getLongName() {
		return longName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setLongName(java.lang.String)
	 */

	public void setLongName(String longName) {
		this.longName = longName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getDesc()
	 */

	public String getDesc() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setDesc(java.lang.String)
	 */

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getType()
	 */

	public RouteType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setType(me.transit.database.impl.RouteImpl.RouteType)
	 */

	public void setType(RouteType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getUrl()
	 */

	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setUrl(java.lang.String)
	 */

	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getColor()
	 */

	public String getColor() {
		return color;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setColor(java.lang.String)
	 */

	public void setColor(String color) {
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getTextColor()
	 */

	public String getTextColor() {
		return textColor;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setTextColor(java.lang.String)
	 */

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getTripList()
	 */

	public List<Trip> getTripList() {
		return this.trips;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setTripList(java.util.List)
	 */

	public void setTripList(List<Trip> list) {
		this.trips = list;
	}

	/*
	 * (non-Javadoc)
	 * @see me.transit.database.impl.TransitDateImpl#toString()
	 */

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
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#valid()
	 */

	public boolean valid() {
		return true;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#toDocument()
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

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#handleEnum(java.lang.String, java.lang.Object)
	 */

	public void handleEnum(String key, Object value) {
		if (key.equals(Route.TYPE)) {
			this.setType(Route.RouteType.valueOf(value.toString()));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see me.database.neo4j.IGraphNode#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		Map<String, String> node = new HashMap<>();
		node.put(FIELD.route.name(), makeKey());
		node.put(FIELD.db_name.name(), this.getShortName());
		node.put(FIELD.db_id.name(), this.getId());
		node.put(FIELD.className.name(), this.getClass().getSimpleName());
		return node;
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.database.neo4j.AbstractGraphNode#makeKey(me.transit.database.TransitData)
	 */
	@Override
	public String makeKey() {		
		StringBuffer key = new StringBuffer();
		key.append(getShortName());
		key.append("@");
		key.append(getAgency().getName());
		return key.toString();
	}

}
