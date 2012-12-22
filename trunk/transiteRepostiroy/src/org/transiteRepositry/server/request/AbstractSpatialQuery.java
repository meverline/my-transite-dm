package org.transiteRepositry.server.request;

import org.transiteRepositry.server.request.utils.LatLonBox;

import com.thoughtworks.xstream.annotations.XStreamAlias;


public class AbstractSpatialQuery {

	@XStreamAlias("agencyName")
	private String    agency = "";
	@XStreamAlias("LatLonBox")
	private LatLonBox latLonBox = null;
	
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
	 * @return the latLonBox
	 */
	public LatLonBox getLatLonBox() {
		return latLonBox;
	}
	/**
	 * @param latLonBox the latLonBox to set
	 */
	public void setLatLonBox(LatLonBox latLonBox) {
		this.latLonBox = latLonBox;
	}

}
