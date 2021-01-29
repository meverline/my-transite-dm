package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.transit.database.RouteGeometry;
import org.springframework.transaction.annotation.Transactional;

@Repository(value="routeGeometryDao")
@Scope("singleton")
@Transactional
public class RouteGeometryDao extends TransitDao<RouteGeometry>  {

	/**
	 * 
	 * @param aSessionFactory
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public RouteGeometryDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(RouteGeometry.class, aSessionFactory);
	}

	@Transactional(readOnly = true)
	public RouteGeometry findGeometryById(String id, String agencyName) throws DaoException
	{
		return this.loadById(id, agencyName);
	}

}
