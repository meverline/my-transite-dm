 package me.transit.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.database.mongo.IDocument;
import me.database.neo4j.AbstractGraphNode;
import me.database.neo4j.FIELD;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.database.converters.RouteTripToString;
import me.transit.json.AgencyToString;

@Entity
@Table(name = "tran_route")
@DiscriminatorColumn(name = "route_type")
@DiscriminatorValue("Route")
@GTFSFileModel(filename="routes.txt")
public class Route extends AbstractGraphNode implements TransitData, IRoute {
	
	public final static String TRIPLIST = "tripList";
	public static final String SHORTNAME = "shortName";
	public static final String LONGNAME = "longName";
	public static final String DESC = "desc";
	public static final String TYPE = "type";
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ROUTE_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	@Column(name = "ID", nullable = false)
	private String id = null;

	@Column(name = "VERSION")
	private String version = "0.5";

	@Column(name = "SHORT_NAME", nullable = false)
	private String shortName = "";

	@Column(name = "LONG_NAME", nullable = false)
	private String longName = "";

	@Column(name = "DESCRIPTION", columnDefinition = "text")
	private String desc = "";

	@Column(name = "TYPE", nullable = false)
	private RouteType type = RouteType.UNKOWN;

	@Column(name = "URL")
	private String url = "";

	@Column(name = "COLOR")
	private String color = "";

	@Column(name = "TEXT_COLOR")
	private String textColor = "";
	
	@Column(name = "SORT_ORDER")
	private int sortOrder = 0;

	@Column(name = "ROUTE_TRIP")
	@Convert(converter = RouteTripToString.class)
	private List<RouteTrip> trips = new ArrayList<>();

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getUUID()
	 */
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setUUID(long)
	 */
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getAgency()
	 */
	@JsonGetter("agency_name")
	@JsonSerialize(converter = AgencyToString.class)
	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setAgency(me.transit.database.impl.Agency)
	 */
	@JsonSetter("agency_name")
	@GTFSSetter(column="agency_id")
	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getId()
	 */
	@JsonGetter("route_id")
	public String getRouteId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setId(java.lang.String)
	 */
	@GTFSSetter(column="route_id")
	@JsonSetter("route_id")
	public void setRouteId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getVersion()
	 */
	@JsonGetter("version")
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setVersion(java.lang.String)
	 */
	@GTFSSetter(column="version")
	@JsonSetter("version")
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getShortName()
	 */
	@JsonGetter("route_short_name")
	public String getShortName() {
		return shortName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setShortName(java.lang.String)
	 */
	@GTFSSetter(column="route_short_name")
	@JsonSetter("route_short_name")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getLongName()
	 */
	@JsonGetter("route_long_name")
	public String getLongName() {
		return longName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setLongName(java.lang.String)
	 */
	@GTFSSetter(column="route_long_name")
	@JsonSetter("route_long_name")
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getDesc()
	 */
	@JsonGetter("route_desc")
	public String getDesc() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setDesc(java.lang.String)
	 */
	@GTFSSetter(column="route_desc")
	@JsonSetter("route_desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getType()
	 */
	@JsonGetter("route_type")
	public RouteType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setType(me.transit.database.impl.RouteImpl.RouteType)
	 */
	@GTFSSetter(column="route_type")
	@JsonSetter("route_type")
	public void setType(RouteType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getUrl()
	 */
	@JsonGetter("url")
	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setUrl(java.lang.String)
	 */
	@GTFSSetter(column="route_url")
	@JsonSetter("url")
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getColor()
	 */
	@JsonGetter("route_color")
	public String getColor() {
		return color;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setColor(java.lang.String)
	 */
	@GTFSSetter(column="route_color")
	@JsonSetter("route_color")
	public void setColor(String color) {
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getTextColor()
	 */
	@JsonGetter("route_text_color")
	public String getTextColor() {
		return textColor;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setTextColor(java.lang.String)
	 */
	@GTFSSetter(column="route_text_color")
	@JsonSetter("route_text_color")
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getUrl()
	 */
	@JsonGetter("route_sort_order")
	public int getSortOrder() {
		return sortOrder;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setUrl(java.lang.String)
	 */
	@GTFSSetter(column="route_sort_order")
	@JsonSetter("route_sort_order")
	public void setSortOrder(int order) {
		this.sortOrder = order;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#getTripList()
	 */

	public List<RouteTrip> getTripList() {
		return this.trips;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setTripList(java.util.List)
	 */

	public void setTripList(List<RouteTrip> list) {
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

	@Override
	@Transient
	public String getId() {
		return this.getRouteId();
	}

	@Override
	@Transient
	public void setId(String id) {
		this.setRouteId(id);
	}
	
}
