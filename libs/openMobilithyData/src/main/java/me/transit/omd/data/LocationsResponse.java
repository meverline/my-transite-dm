package me.transit.omd.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class LocationsResponse {

	private LocationsResponse.Results results;
	private String status;
	private int timestamp;

	/**
	 * @return the status
	 */
	@JsonGetter("status")
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@JsonSetter("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the timestamp
	 */
	@JsonGetter("ts")
	public int getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	@JsonSetter("ts")
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the results
	 */
	@JsonGetter("results")
	public LocationsResponse.Results getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	@JsonSetter("results")
	public void setResults(LocationsResponse.Results results) {
		this.results = results;
	}

	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	public static class Results {
		List<Location> locations = new ArrayList<>();

		/**
		 * @return the locations
		 */
		@JsonGetter("locations")
		public List<Location> getLocations() {
			return locations;
		}

		/**
		 * @param locations
		 *            the locations to set
		 */
		@JsonSetter("locations")
		public void setLocations(List<Location> locations) {
			this.locations = locations;
		}

	}
}
