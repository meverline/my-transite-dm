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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Geometry;

import me.transit.database.RouteGeometry;

@Entity
@Table(name="tran_route_geometry")
@Inheritance
@XStreamAlias("RouteGeometry")
@DiscriminatorColumn(name = "routeGeometry_type")
@DiscriminatorValue("RouteGeometryImpl")
public class RouteGeometry implements TransitData {

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
	
	@Column(name="SHAPE", columnDefinition = "Geometry")
	@Type(type="jts_geometry")
	@XStreamAlias("shape")
	@XStreamConverter(me.database.LineStringConverter.class)
	private Geometry shape = null;

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getUUID()
	 */

	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setUUID(long)
	 */

	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getAgency()
	 */

	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setAgency(me.transit.database.impl.Agency)
	 */

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getId()
	 */

	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setId(java.lang.String)
	 */

	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getVersion()
	 */

	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setVersion(java.lang.String)
	 */

	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#getShape()
	 */
	

	public Geometry getShape() {
		return shape;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#setShape(com.vividsolutions.jts.geom.Geometry)
	 */

	public void setShape(Geometry shape) {
		this.shape = shape;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.RouteGeometry#valid()
	 */

	public boolean valid() 
	{
		return true;
	}
	
}
