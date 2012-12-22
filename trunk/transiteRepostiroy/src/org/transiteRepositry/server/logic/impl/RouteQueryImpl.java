/**
 * 
 */
package org.transiteRepositry.server.logic.impl;

import java.util.ArrayList;
import java.util.List;

import me.transit.dao.DaoException;
import me.transit.dao.RouteDao;
import me.transit.dao.RouteGeometryDao;
import me.transit.database.Route;
import me.transit.database.RouteGeometry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.transiteRepositry.server.logic.RouteQuery;
import org.transiteRepositry.server.request.RouteGeometryRequest;
import org.transiteRepositry.server.request.RouteRequest;
import org.transiteRepositry.server.request.utils.RouteGeometryTuple;
import org.transiteRepositry.server.request.utils.RouteTuple;


/**
 * @author meverline
 *
 */
public class RouteQueryImpl implements RouteQuery {

	private static Log log = LogFactory.getLog(RouteQueryImpl.class);
	
	private RouteDao routeDao = null;
	private RouteGeometryDao routeGeometryDao = null;
	
	/**
	 * @return the routeDao
	 */
	public RouteDao getRouteDao() {
		return routeDao;
	}

	/**
	 * @param routeDao the routeDao to set
	 */
	public void setRouteDao(RouteDao routeDao) {
		this.routeDao = routeDao;
	}

	/**
	 * @return the routeGeometryDao
	 */
	public RouteGeometryDao getRouteGeometryDao() {
		return routeGeometryDao;
	}

	/**
	 * @param routeGeometryDao the routeGeometryDao to set
	 */
	public void setRouteGeometryDao(RouteGeometryDao routeGeometryDao) {
		this.routeGeometryDao = routeGeometryDao;
	}

	/* (non-Javadoc)
	 * @see server.logic.RouteQuery#query(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Route> query(String agency, String shortName) {

		try {
			return getRouteDao().findByRouteNumber(shortName, agency);
		} catch (DaoException e) {
			log.error(e);
		}
		return new ArrayList<Route>();
	}

	/* (non-Javadoc)
	 * @see server.logic.RouteQuery#query(server.request.RouteRequest)
	 */
	@Override
	public List<Route> query(RouteRequest request) {
		List<Route> rtn = new ArrayList<Route>();
		for ( RouteTuple tuple : request.getRouteTuples()) {
			List<Route> result = query(tuple.getAgency(), tuple.getShortName());
			rtn.addAll(result);
		}
		return rtn;
	}

	/* (non-Javadoc)
	 * @see server.logic.RouteQuery#getGeometry(java.lang.String, long)
	 */
	@Override
	public RouteGeometry getGeometry(String agency, long shapeId) {

		try {
			return getRouteGeometryDao().findGeometryById(shapeId, agency);
		} catch (DaoException e) {
			log.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see server.logic.RouteQuery#getGeometry(server.request.RouteGeometryRequest)
	 */
	@Override
	public List<RouteGeometry> getGeometry(RouteGeometryRequest request) {
		
		List<RouteGeometry> rtn = new ArrayList<RouteGeometry>();
		for ( RouteGeometryTuple data : request.getRouteGeometryTuple()) {
			RouteGeometry geo = getGeometry(data.getAgency(), data.getShapeId());
			if ( geo != null ) {
				rtn.add(geo);
			}
		}
		return rtn;
	}

}
