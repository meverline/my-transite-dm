package me.transit.database;

import java.io.Serializable;

public interface TransitData extends Serializable {

	/**
	 * @return the uuid
	 */
	public long getUUID();
	
	/**
	 * @return the uuid
	 */
	public void setUUID(long id);
	
	/**
	 * 
	 * @return
	 */
	public String getVersion();
	
	/**
	 * 
	 * @param id
	 */
	public void setVersion(String id);
	
	/**
	 * @return the agencyId
	 */
	public Agency getAgency();

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgency(Agency agencyId);
	
	/**
	 * @return the serviceId
	 */
	public String getId();

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setId(String id);
	
	/**
	 * 
	 * @return
	 */
	public boolean valid();

}