package me.transit.parser.omd;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class FeedsResponse {

	private FeedsResponse.Results results;
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
	public FeedsResponse.Results getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	@JsonSetter("results")
	public void setResults(FeedsResponse.Results results) {
		this.results = results;
	}

	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	public static class Results {
		List<Feed> feeds = new ArrayList<>();
		private int total;
		private int limit;
		private int page;
		private int numpages;

		/**
		 * @return the total
		 */
		@JsonGetter("total")
		public int getTotal() {
			return total;
		}

		/**
		 * @param total
		 *            the total to set
		 */
		@JsonSetter("total")
		public void setTotal(int total) {
			this.total = total;
		}

		/**
		 * @return the limit
		 */
		@JsonGetter("limit")
		public int getLimit() {
			return limit;
		}

		/**
		 * @param limit
		 *            the limit to set
		 */
		@JsonSetter("limit")
		public void setLimit(int limit) {
			this.limit = limit;
		}

		/**
		 * @return the page
		 */
		@JsonGetter("page")
		public int getPage() {
			return page;
		}

		/**
		 * @param page
		 *            the page to set
		 */
		@JsonSetter("page")
		public void setPage(int page) {
			this.page = page;
		}

		/**
		 * @return the numpages
		 */
		@JsonGetter("numPages")
		public int getNumpages() {
			return numpages;
		}

		/**
		 * @param numpages
		 *            the numpages to set
		 */
		@JsonSetter("numPages")
		public void setNumpages(int numpages) {
			this.numpages = numpages;
		}

		/**
		 * @return the locations
		 */
		@JsonGetter("feeds")
		public List<Feed> getFeeds() {
			return feeds;
		}

		/**
		 * @param locations
		 *            the locations to set
		 */
		@JsonSetter("feeds")
		public void setFeeds(List<Feed> locations) {
			this.feeds = locations;
		}

	}
}
