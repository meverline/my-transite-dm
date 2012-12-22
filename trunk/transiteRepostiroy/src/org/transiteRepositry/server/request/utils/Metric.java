package org.transiteRepositry.server.request.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Metric")
public class Metric {

	@XStreamAlias("metricData")
	private String metricData = null;
	@XStreamAlias("metricType")
	private String metricType = null;

	/**
	 * @return the metricData
	 */
	public String getMetricData() {
		return metricData;
	}
	/**
	 * @param metricData the metricData to set
	 */
	public void setMetricData(String metricData) {
		this.metricData = metricData;
	}
	/**
	 * @return the metricType
	 */
	public String getMetricType() {
		return metricType;
	}
	/**
	 * @param metricType the metricType to set
	 */
	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

}
