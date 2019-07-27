package me.transit.database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Polygon;

import me.database.neo4j.FIELD;
import me.database.neo4j.AbstractGraphNode;

@Entity
@Table(name = "tran_agency")
@Inheritance
@DiscriminatorColumn(name = "angency_type")
@DiscriminatorValue("AgencyImpl")
@XStreamAlias("Agency")
public class Agency extends AbstractGraphNode implements Serializable {

	public static final String AGENCY = "agency";
	public static final String UUID = "uuid";
	
	/**
	 * 
	 */
	@XStreamOmitField
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AGENCY_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XStreamOmitField
	private long uuid = -1;

	@Column(name = "NAME", nullable = false)
	@XStreamAlias("name")
	private String name = "";

	@Column(name = "URL", nullable = false)
	@XStreamAlias("url")
	private String url = "";

	@Column(name = "TIMEZONE", nullable = false)
	@XStreamAlias("timeZone")
	private String timezone = "";

	@Column(name = "LANG")
	@XStreamAlias("lang")
	private String lang = "";

	@Column(name = "PHONE")
	@XStreamAlias("phone")
	private String phone = "";

	@Column(name = "ID")
	@XStreamOmitField
	private String id = "";

	@Column(name = "VERSION")
	@XStreamAlias("version")
	private String version = "0.5";

	@Column(name = "MBR", columnDefinition = "Geometry")
	@Type(type="jts_geometry")
	@XStreamAlias("mbr")
	private Polygon mbr = null;

	private String fareUrl = null;

	public Agency() {

	}

	public Agency(String name) {
		setName(name);
	}

	/**
	 * 
	 * @return
	 */
	public long getUUID() {
		return uuid;
	}

	/**
	 * 
	 * @param id
	 */
	public void setUUID(long id) {
		uuid = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getVersion()
	 */
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setVersion(java.lang.String)
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getUrl()
	 */
	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setUrl(java.lang.String)
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getTimezone()
	 */
	public String getTimezone() {
		return timezone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setTimezone(java.lang.String)
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getLang()
	 */
	public String getLang() {
		return lang;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setLang(java.lang.String)
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getPhone()
	 */
	public String getPhone() {
		return phone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setPhone(java.lang.String)
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getMBR()
	 */
	public Polygon getMBR() {
		return this.mbr;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setMBR(com.vividsolutions.jts.geom.Polygon)
	 */
	public void setMBR(Polygon mbr) {
		this.mbr = mbr;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#toString()
	 */
	public String toString() {
		return this.getName();
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {

		boolean rtn = false;
		if (Agency.class.isAssignableFrom(obj.getClass())) {
			Agency rhs = Agency.class.cast(obj);
			rtn = true;
			if (!this.getName().equals(rhs.getName())) {
				rtn = false;
			}
			if (!this.getUrl().equals(rhs.getUrl())) {
				rtn = false;
			}
			if (!this.getPhone().equals(rhs.getPhone())) {
				rtn = false;
			}
		}
		return rtn;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setFareUrl(java.lang.String)
	 */
	public void setFareUrl(String url) {
		fareUrl = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getFareUrl()
	 */
	public String getFareUrl() {
		return fareUrl;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#valid()
	 */
	public boolean valid() 
	{
		return true;
	}

	@Override
	public Map<String, String> getProperties() {
		Map<String, String> node = new HashMap<>();
	    node.put(FIELD.agency.name(), this.getName());
	    node.put(FIELD.db_id.name(), this.getId());
	    node.put(FIELD.className.name(), this.getClass().getName());		
		return node;
	}
	
}
