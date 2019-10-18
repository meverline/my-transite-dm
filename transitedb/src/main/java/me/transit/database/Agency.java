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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Polygon;

import me.database.neo4j.AbstractGraphNode;
import me.database.neo4j.FIELD;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.json.Base64StringToGeometry;
import me.transit.json.GeometryToBase64String;

@Entity
@Table(name = "tran_agency")
@Inheritance
@DiscriminatorColumn(name = "angency_type")
@DiscriminatorValue("AgencyImpl")
@GTFSFileModel(filename="agency.txt")
public class Agency extends AbstractGraphNode implements Serializable {

	public static final String AGENCY = "agency";
	public static final String UUID = "uuid";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AGENCY_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uuid = -1;

	@Column(name = "NAME", nullable = false)
	private String name = "";

	@Column(name = "URL", nullable = false)
	private String url = "";

	@Column(name = "TIMEZONE", nullable = false)
	private String timezone = "";

	@Column(name = "LANG")
	private String lang = "";

	@Column(name = "PHONE")
	private String phone = "";

	@Column(name = "ID")
	private String id = "";

	@Column(name = "VERSION")
	private String version = "0.5";

	@Column(name = "MBR", columnDefinition = "Geometry")
	@Type(type="jts_geometry")
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
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/**
	 * 
	 * @param id
	 */
	@JsonSetter("uuid")
	public void setUUID(long id) {
		uuid = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getVersion()
	 */
	@JsonGetter("version")
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setVersion(java.lang.String)
	 */
	@GTFSSetter(column="version")
	@JsonSetter("version")
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getName()
	 */
	@JsonGetter("agency_name")
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setName(java.lang.String)
	 */
	@GTFSSetter(column="agency_name")
	@JsonSetter("agency_name")
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getUrl()
	 */
	@JsonGetter("agency_url")
	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setUrl(java.lang.String)
	 */
	@GTFSSetter(column="agency_url")
	@JsonSetter("agency_url")
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getTimezone()
	 */
	@JsonGetter("agency_timezone")
	public String getTimezone() {
		return timezone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setTimezone(java.lang.String)
	 */
	@GTFSSetter(column="agency_timezone")
	@JsonSetter("agency_timezone")
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getLang()
	 */
	@JsonGetter("agency_lang")
	public String getLang() {
		return lang;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setLang(java.lang.String)
	 */
	@GTFSSetter(column="agency_lang")
	@JsonSetter("agency_lang")
	public void setLang(String lang) {
		this.lang = lang;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getPhone()
	 */
	@JsonGetter("agency_phone")
	public String getPhone() {
		return phone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setPhone(java.lang.String)
	 */
	@GTFSSetter(column="agency_phone")
	@JsonSetter("agency_phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getId()
	 */
	@JsonGetter("agency_id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setId(java.lang.String)
	 */
	@GTFSSetter(column="agency_id")
	@JsonSetter("agency_id")
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getMBR()
	 */
	@JsonGetter("mbr")
	@JsonSerialize(converter = GeometryToBase64String.class)
	public Polygon getMBR() {
		return this.mbr;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setMBR(com.vividsolutions.jts.geom.Polygon)
	 */
	@JsonSetter("mbr")
	@JsonDeserialize(converter = Base64StringToGeometry.class)
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
			if (this.getName() != null && ! this.getName().equals(rhs.getName())) {
				rtn = false;
			}
			if (this.getUrl() != null && ! this.getUrl().equals(rhs.getUrl())) {
				rtn = false;
			}
			if (this.getPhone() != null && ! this.getPhone().equals(rhs.getPhone())) {
				rtn = false;
			}
		}
		return rtn;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#setFareUrl(java.lang.String)
	 */
	@GTFSSetter(column="agency_fareUrl")
	@JsonSetter("agency_fareUrl")
	public void setFareUrl(String url) {
		fareUrl = url;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.Agency#getFareUrl()
	 */
	@JsonGetter("agency_fareUrl")
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
