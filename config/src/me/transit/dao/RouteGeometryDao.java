package me.transit.dao;

import me.transit.database.RouteGeometry;


public interface RouteGeometryDao extends TransitDao {

	public abstract RouteGeometry findGeometryById(long id, String agencyName) throws DaoException;

}