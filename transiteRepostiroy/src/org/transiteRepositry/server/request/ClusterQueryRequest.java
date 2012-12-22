package org.transiteRepositry.server.request;

import org.transiteRepositry.server.request.utils.Cluster;
import org.transiteRepositry.server.request.utils.Distance;
import org.transiteRepositry.server.request.utils.Metric;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("ClusterQueryRequest")
public class ClusterQueryRequest extends AbstractSpatialQuery {

	@XStreamAlias("Metric")
	private Metric metric = null;
	@XStreamAlias("Cluster")
	private Cluster cluster = null;
	@XStreamAlias("Distance")
	private Distance distance = null;
	
	/**
	 * @return the cluster
	 */
	public Cluster getCluster() {
		return cluster;
	}
	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	/**
	 * @return the distance
	 */
	public Distance getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Distance distance) {
		this.distance = distance;
	}
	/**
	 * @return the metric
	 */
	public Metric getMetric() {
		return metric;
	}
	/**
	 * @param metric the metric to set
	 */
	public void setMetric(Metric metric) {
		this.metric = metric;
	}
	
}
