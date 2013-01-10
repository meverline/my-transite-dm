package me.transit.dao;

import me.transit.database.RouteGeometry;


public interface RouteGeometryDao extends TransitDao<RouteGeometry> {

	public abstract RouteGeometry findGeometryById(String id, String agencyName) throws DaoException;

}