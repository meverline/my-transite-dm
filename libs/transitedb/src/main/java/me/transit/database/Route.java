 package me.transit.database;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.database.nsstore.IDocument;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;

@Entity
@Table(name = "tran_route")
@DiscriminatorColumn(name = "route_type")
@DiscriminatorValue("Route")
@GTFSFileModel(filename="routes.txt")
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Route  extends TransitData implements IRoute, IDocument {
	
	public final static String TRIPLIST = "tripList";
	public static final String SHORTNAME = "shortName";
	public static final String TYPE = "type";
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ROUTE_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;

	@Column(name = "ID", nullable = false)
	private String id = null;

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROUTE_UUID", nullable = false, updatable = false)
	@OrderBy("TRIP_NDX")
	private List<Trip> trips = new ArrayList<>();
	
	private transient long docId = -1;

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

	@JsonGetter("trips")
	public List<Trip> getTripList() {
		return this.trips;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Route#setTripList(java.util.List)
	 */

	@JsonSetter("trips")
	public void setTripList(List<Trip> list) {
		this.trips = list;
	}

	/*
	 * (non-Javadoc)
	 * @see me.transit.database.impl.TransitDateImpl#toString()
	 */

	@Override
	public String toString() {
		return "Route{" +
				"uuid=" + uuid +
				", agency=" + getAgency() +
				", id='" + id + '\'' +
				", version='" + getVersion() + '\'' +
				", shortName='" + shortName + '\'' +
				", longName='" + longName + '\'' +
				", desc='" + desc + '\'' +
				", type=" + type +
				", url='" + url + '\'' +
				", color='" + color + '\'' +
				", textColor='" + textColor + '\'' +
				", sortOrder=" + sortOrder +
				", trips=" + trips +
				", docId='" + docId + '\'' +
				'}';
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
	
	@JsonGetter("_id")
	public long getDocId() {
		return docId;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Trip#setUUID(long)
	 */
	@JsonSetter("_id")
	public void setDocId(long uuid) {
		this.docId = uuid;
	}

}
