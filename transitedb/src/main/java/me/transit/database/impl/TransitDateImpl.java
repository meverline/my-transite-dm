package me.transit.database.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import me.transit.database.Agency;
import me.transit.database.TransitData;

public abstract class TransitDateImpl implements TransitData {

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
	 * @return the agencyId
	 */

	public Agency getAgency() {
		return agency;
	}

	/**
	 * @param agencyId
	 *            the agencyId to set
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
	 * @param serviceId
	 *            the serviceId to set
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
