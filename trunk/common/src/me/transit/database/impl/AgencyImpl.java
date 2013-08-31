package me.transit.database.impl;

import me.transit.database.Agency;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
	 * @param version the version to set
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
	 * @param name the name to set
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
	 * @param url the url to set
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
	 * @param timezone the timezone to set
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
	 * @param lang the lang to set
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
	
}
