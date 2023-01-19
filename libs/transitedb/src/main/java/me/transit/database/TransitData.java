package me.transit.database;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import me.transit.annotation.GTFSSetter;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class TransitData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VERSION")
	private String version = "0.5";

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	/**
	 * @return the uuid
	 */
	public abstract long getUUID();
	
	/**
	 * @return the uuid
	 */
	public abstract void setUUID(long id);

	/**
	 * @return the serviceId
	 */
	public abstract String getId();

	/**
	 * @param id the serviceId to set
	 */
	public abstract void setId(String id);

	/**
	 *
	 * @return
	 */
	public abstract boolean valid();

	/**
	 * 
	 * @return
	 */
	@JsonGetter("version")
	public String getVersion() {
		return this.version;
	};
	
	/**
	 * 
	 * @param id
	 */
	@GTFSSetter(column="version")
	@JsonSetter("version")
	public void setVersion(String id) {
		this.version = id;
	};


	@JsonGetter("agency_name")
	public Agency getAgency() {
		return agency;
	}

	@JsonSetter("agency_name")
	@GTFSSetter(column="agency_id")
	public void setAgency(Agency agency) {
		this.agency = agency;
	}
}