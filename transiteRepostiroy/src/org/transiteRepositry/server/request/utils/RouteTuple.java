package org.transiteRepositry.server.request.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RouteTuple")
public class RouteTuple {
	
	@XStreamAlias("agencyName")
	private String agency = null;
	@XStreamAlias("shortName")
	private String shortName = null;
	
	/**
	 * @return the agency
	 */
	public String getAgency() {
		return agency;
	}
	/**
	 * @param agency the agency to set
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
}
