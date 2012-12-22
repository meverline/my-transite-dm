package org.transiteRepositry.server.response;

import java.util.List;

import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TransiteStopQueryResponse")
public class TransiteStopQueryResponse {

	private List<TransitStop> stops = null;
	
	/**
	 * @return the stops
	 */
	public List<TransitStop> getStops() {
		return stops;
	}

	/**
	 * @param stops the stops to set
	 */
	public void setStops(List<TransitStop> stops) {
		this.stops = stops;
	}

	public TransiteStopQueryResponse() {}
	
	public TransiteStopQueryResponse(List<TransitStop> stopList)
	{
		stops = stopList;
	}
	
	
}
