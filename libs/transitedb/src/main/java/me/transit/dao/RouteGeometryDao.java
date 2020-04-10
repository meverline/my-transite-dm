package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.transit.database.RouteGeometry;

@Repository(value="routeGeometryDao")
@Scope("singleton")
public class RouteGeometryDao extends TransitDao<RouteGeometry>  {

	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public RouteGeometryDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(RouteGeometry.class, aSessionFactory);
	}
	
	public RouteGeometry findGeometryById(String id, String agencyName) throws DaoException
	{
		return this.loadById(id, agencyName);
	}

}
