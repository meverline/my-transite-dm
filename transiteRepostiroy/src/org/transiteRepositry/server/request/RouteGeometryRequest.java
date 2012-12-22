package org.transiteRepositry.server.request;

import java.util.List;

import org.transiteRepositry.server.request.utils.RouteGeometryTuple;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


@XStreamAlias("RouteGeometryRequest")
public class RouteGeometryRequest {

	@XStreamImplicit(itemFieldName="Shape")
	private List<RouteGeometryTuple> routeGeometryTuple = null;

	/**
	 * @return the routeGeometryTuple
	 */
	public List<RouteGeometryTuple> getRouteGeometryTuple() {
		return routeGeometryTuple;
	}

	/**
	 * @param routeGeometryTuple the routeGeometryTuple to set
	 */
	public void setRouteGeometryTuple(List<RouteGeometryTuple> routeGeometryTuple) {
		this.routeGeometryTuple = routeGeometryTuple;
	}
		
}
