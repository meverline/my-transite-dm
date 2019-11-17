package me.transit.database;

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
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;

import me.transit.annotation.GTFSSetter;
import me.transit.json.AgencyToString;
import me.transit.json.Base64StringToGeometry;
import me.transit.json.GeometryToBase64String;

@Entity
@Table(name="tran_route_geometry")
@Inheritance
@DiscriminatorColumn(name = "routeGeometry_type")
@DiscriminatorValue("RouteGeometryImpl")
public class RouteGeometry implements TransitData {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uuid = -1;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	@Column(name = "ID", nullable = false)
	private String id = null;

	@Column(name = "VERSION")
	private String version = "0.5";
	
	@Column(name="SHAPE", columnDefinition = "Geometry")
	@Type(type="jts_geometry")
	private Geometry shape = null;

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getUUID()
	 */
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setUUID(long)
	 */
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getAgency()
	 */
	@JsonGetter("agency_name")
	@JsonSerialize(converter = AgencyToString.class)
	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setAgency(me.transit.database.impl.Agency)
	 */
	@JsonSetter("agency_name")
	public void setAgency(Agency agency) {
		this.agency = agency;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getId()
	 */
	@JsonGetter("route_id")
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setId(java.lang.String)
	 */
	@GTFSSetter(column="route_id")
	@JsonSetter("route_id")
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getVersion()
	 */
	@JsonGetter("version")
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setVersion(java.lang.String)
	 */
	@GTFSSetter(column="version")
	@JsonSetter("version")
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getShape()
	 */
	@JsonGetter("shape")
	@JsonSerialize(converter = GeometryToBase64String.class)
	public Geometry getShape() {
		return shape;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setShape(com.vividsolutions.jts.geom.Geometry)
	 */
	@GTFSSetter(column="shape")
	@JsonSetter("shape")
	@JsonDeserialize(converter = Base64StringToGeometry.class)
	public void setShape(Geometry shape) {
		this.shape = shape;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#valid()
	 */

	public boolean valid() 
	{
		if ( this.getAgency() == null && this.getId() != null) {
			return false;
		}
		return true;
	}
	
}
