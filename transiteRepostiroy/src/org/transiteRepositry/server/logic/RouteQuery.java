package org.transiteRepositry.server.logic;

import java.util.List;

import org.transiteRepositry.server.request.RouteGeometryRequest;
import org.transiteRepositry.server.request.RouteRequest;


import me.transit.database.Route;
import me.transit.database.RouteGeometry;

public interface RouteQuery {

	public List<Route> query(String agency, String shortName);
	
	public List<Route> query(RouteRequest request);
	
	public RouteGeometry getGeometry(String agency, long shapeId);
	
	public List<RouteGeometry> getGeometry(RouteGeometryRequest request);
	
}
