package org.transiteRepositry.server.request;

import java.util.List;

import org.transiteRepositry.server.request.utils.RouteTuple;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("RouteRequest")
public class RouteRequest {

	private List<RouteTuple> routeTuples = null;

	/**
	 * @return the routeTuples
	 */
	public List<RouteTuple> getRouteTuples() {
		return routeTuples;
	}

	/**
	 * @param routeTuples the routeTuples to set
	 */
	public void setRouteTuples(List<RouteTuple> routeTuples) {
		this.routeTuples = routeTuples;
	}

}
