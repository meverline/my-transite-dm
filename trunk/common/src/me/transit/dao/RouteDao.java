package me.transit.dao;

import java.util.List;

import me.transit.database.Route;

public interface RouteDao extends TransitDao<Route> {

	/**
	 * 
	 * @param constraint
	 * @return
	 */
	public abstract List<Route> findByRouteNumber(String routeNumber,
			String agencyName) throws DaoException;

}