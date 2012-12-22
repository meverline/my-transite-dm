package org.transiteRepositry.server.response;

import java.util.List;

import me.math.grid.SpatialGridPoint;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ClusterQueryReponse")
public class ClusterQueryReponse {
	
	private List<SpatialGridPoint> clusters = null;

	public ClusterQueryReponse() {}
	
	public ClusterQueryReponse(List<SpatialGridPoint> points)
	{
		clusters = points;
	}

	/**
	 * @return the clusters
	 */
	public List<SpatialGridPoint> getClusters() {
		return clusters;
	}

	/**
	 * @param clusters the clusters to set
	 */
	public void setClusters(List<SpatialGridPoint> clusters) {
		this.clusters = clusters;
	}
	
	
}
