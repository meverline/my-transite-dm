package me.transit.database.impl;

import me.transit.database.Agency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Polygon;

@Entity
@Table(name="tran_agency")
public class AgencyImpl implements Agency {
	
	public static final String AGENCY = "agency";
	public static final String UUID = "uuid";

	/**
	 * 
	 */
	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	
	@XStreamOmitField
	private long uuid = -1;
	
	@XStreamAlias("name")
	private String name = "";
	
	@XStreamAlias("url")
	private String url = "";
	
	@XStreamAlias("timeZone")
	private String timezone = "";
	
	@XStreamAlias("lang")
	private String lang = "";
	
	@XStreamAlias("phone")
	private String phone = "";
	
	@XStreamOmitField
	private String id = "";
	
	@XStreamAlias("version")
	private String version = "0.5";
	
	@XStreamAlias("mbr")
	private Polygon mbr = null;
	
	private String fareUrl = null;
	
	public AgencyImpl()
	{
		
	}
	
	public AgencyImpl(String name)
	{
		setName(name);
	}
	
	/**
	 * @return the uuid
	 */
	@Id
	@Column(name="AGENCY_UUID", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	@Column(name="VERSION")
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
	 * @return the name
	 */
	@Column(name="NAME", nullable=false)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	@Column(name="URL", nullable=false)
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the timezone
	 */
	@Column(name="TIMEZONE", nullable=false)
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the lang
	 */
	@Column(name="LANG")
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the phone
	 */
	@Column(name="PHONE")
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
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
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.Agency#getMBR()
	 */
	@Override
	@Column(name="MBR", columnDefinition = "Geometry")
	@Type(type = "org.hibernate.spatial.GeometryType")
	public Polygon getMBR() {
		return this.mbr;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.Agency#setMBR(com.vividsolutions.jts.geom.Polygon)
	 */
	@Override
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
		if ( Agency.class.isAssignableFrom(obj.getClass()) ) {
			Agency rhs = Agency.class.cast(obj);				
			rtn = true;
			if ( ! this.getName().equals(rhs.getName() )) {
				rtn = false;
			}
			if ( ! this.getUrl().equals(rhs.getUrl()) ) {
				rtn = false;
			}
			if ( ! this.getPhone().equals(rhs.getPhone()) ) {
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
	
}
