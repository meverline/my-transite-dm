package me.transit.database.impl;

import java.util.HashMap;
import java.util.Map;

import me.transit.dao.mongo.IDocument;
import me.transit.database.Agency;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class AgencyImpl implements Agency {

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
	public Map<String, Object> toDocument() {
		Map<String,Object> rtn = new HashMap<String,Object>();
		
		rtn.put(IDocument.CLASS, AgencyImpl.class.getName());
		rtn.put("name", this.getName());
		rtn.put("url", this.getUrl());
		rtn.put(IDocument.ID, this.getName());
		rtn.put("phone", this.getPhone());
		return rtn;
	}
	
	@Override
	public String getCollection() {
		return Agency.COLLECTION;
	}

	@Override
	public void fromDocument(Map<String, Object> map) {
		// TODO Auto-generated method stub	
	}
}
