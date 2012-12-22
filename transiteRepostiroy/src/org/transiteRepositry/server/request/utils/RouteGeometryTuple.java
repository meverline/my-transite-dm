package org.transiteRepositry.server.request.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RouteGeometryTuple")
public class RouteGeometryTuple {
	
	@XStreamAlias("agencyName")
	private String agency = null;
	@XStreamAlias("shapeId")
	private long shapeId = -1;
	
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
	 * @return the shapeId
	 */
	public long getShapeId() {
		return shapeId;
	}
	/**
	 * @param shapeId the shapeId to set
	 */
	public void setShapeId(long shapeId) {
		this.shapeId = shapeId;
	}
	
}
