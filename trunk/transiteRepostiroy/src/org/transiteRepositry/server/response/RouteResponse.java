package org.transiteRepositry.server.response;

import java.util.List;

import me.transit.database.Route;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RouteResponse")
public class RouteResponse {

	private List<Route> routes = null;

	public RouteResponse() {
	}

	public RouteResponse(List<Route> data) {
		routes = data;
	}

	/**
	 * @return the routes
	 */
	public List<Route> getRoutes() {
		return routes;
	}

	/**
	 * @param routes
	 *            the routes to set
	 */
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

}
