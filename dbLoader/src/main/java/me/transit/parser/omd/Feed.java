package me.transit.parser.omd;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Feed {

	private String id;
	private String type;
	private String agencyName;
	private Location location;
	private Feed.URL url;
	private Feed.Latest latest;

	/**
	 * @return the id
	 */
	@JsonGetter("id")
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@JsonSetter("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	@JsonGetter("ty")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	@JsonSetter("ty")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the agencyName
	 */
	@JsonGetter("t")
	public String getAgencyName() {
		return agencyName;
	}

	/**
	 * @param agencyName
	 *            the agencyName to set
	 */
	@JsonSetter("t")
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	/**
	 * @return the location
	 */
	@JsonGetter("l")
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	@JsonSetter("l")
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the url
	 */
	@JsonGetter("u")
	public Feed.URL getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	@JsonSetter("u")
	public void setUrl(Feed.URL url) {
		this.url = url;
	}
	
	/**
	 * @return the latest
	 */
	@JsonGetter("latest")
	public Feed.Latest getLatest() {
		return latest;
	}

	/**
	 * @param latest the latest to set
	 */
	@JsonSetter("latest")
	public void setLatest(Feed.Latest latest) {
		this.latest = latest;
	}

	@Override
	public String toString() {
		return "Feed{" +
				"id='" + id + '\'' +
				", type='" + type + '\'' +
				", agencyName='" + agencyName + '\'' +
				", location=" + location +
				", url=" + url +
				", latest=" + latest +
				'}';
	}

	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	public  class Latest {
		private int timestamp;
		
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

		@Override
		public String toString() {
			return "Latest{" +
					"timestamp=" + timestamp +
					'}';
		}
	}

	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	public  class URL {
		private String website;
		private String gtfsUrl;

		/**
		 * @return the website
		 */
		@JsonGetter("i")
		public String getWebsite() {
			return website;
		}

		/**
		 * @param website
		 *            the website to set
		 */
		@JsonSetter("i")
		public void setWebsite(String website) {
			this.website = website;
		}

		/**
		 * @return the gtfsUrl
		 */
		@JsonGetter("d")
		public String getGtfsUrl() {
			return gtfsUrl;
		}

		/**
		 * @param gtfsUrl
		 *            the gtfsUrl to set
		 */
		@JsonSetter("d")
		public void setGtfsUrl(String gtfsUrl) {
			this.gtfsUrl = gtfsUrl;
		}

		@Override
		public String toString() {
			return "URL{" +
					"website='" + website + '\'' +
					", gtfsUrl='" + gtfsUrl + '\'' +
					'}';
		}
	}
}
