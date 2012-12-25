package me.transit.database;

public interface TransitData {

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
	public long getId();

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setId(long id);

}