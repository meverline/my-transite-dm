package me.transit.database.impl;

import me.transit.database.Agency;
import me.transit.database.TransitData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class TransitDateImpl implements TransitData {
	
	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamAlias("id")
	@JsonProperty("UUID")
	private long uuid = -1;
	private Agency agency = null;
	@XStreamOmitField
	@JsonProperty("Id")
	private String id = null;
	@XStreamAlias("version")
	private String version = "0.5";
	
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
	 * @return the agencyId
	 */
	public Agency getAgency() {
		return agency;
	}

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgency(Agency agencyId) {
		this.agency = agencyId;
	}
	
	/**
	 * @return the serviceId
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("id: " + getId());
		builder.append("\n");
		builder.append("Agency: " + this.getAgency());
		builder.append("\n");
		return builder.toString();
	}
}
