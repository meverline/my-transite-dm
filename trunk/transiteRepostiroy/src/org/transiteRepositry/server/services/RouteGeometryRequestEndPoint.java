package org.transiteRepositry.server.services;

import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.transiteRepositry.server.logic.RouteQuery;
import org.transiteRepositry.server.request.RouteGeometryRequest;
import org.transiteRepositry.server.response.RouteGeometryResponse;


public class RouteGeometryRequestEndPoint extends AbstractMarshallingPayloadEndpoint {

	private RouteQuery routeQueryHandler = null;

	/**
	 * @return the routeQueryHandler
	 */
	public RouteQuery getRouteQueryHandler() {
		return routeQueryHandler;
	}

	/**
	 * @param routeQueryHandler the routeQueryHandler to set
	 */
	public void setRouteQueryHandler(RouteQuery routeQueryHandler) {
		this.routeQueryHandler = routeQueryHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint#invokeInternal(java.lang.Object)
	 */
	@Override
	protected Object invokeInternal(Object arg0) throws Exception {
		RouteGeometryRequest request = RouteGeometryRequest.class.cast(arg0);
		
		return new RouteGeometryResponse(this.getRouteQueryHandler().getGeometry(request));
	}

}
