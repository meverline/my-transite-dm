package me.transit.database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Polygon;

import me.transit.database.Agency;

@Entity
@Table(name = "tran_agency")
@DiscriminatorColumn(name = "angency_type")
@DiscriminatorValue("AgencyImpl")
@XStreamAlias("Agency")
public class Agency implements Serializable{

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
	 * @return the uuid
	 */

	public long getUUID() {
		return uuid;
	}

	/**
	 * @return the uuid
	 */
	public void setUUID(long id) {
		uuid = id;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the name
	 */

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the timezone
	 */

	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the lang
	 */

	public String getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the phone
	 */

	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * the mbr or agency
	 * @return
	 */
	public Polygon getMBR() {
		return this.mbr;
	}

	/**
	 * 
	 * @param mbr
	 */
	public void setMBR(Polygon mbr) {
		this.mbr = mbr;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
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

	public void setFareUrl(String url) {
		fareUrl = url;
	}

	public String getFareUrl() {
		return fareUrl;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean valid() 
	{
		return true;
	}

}
