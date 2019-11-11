package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import me.database.hibernate.HibernateConnection;
import me.transit.database.RouteGeometry;

@Repository(value="routeGeometryDao")
@Scope("singleton")
public class RouteGeometryDao extends TransitDao<RouteGeometry>  {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public RouteGeometryDao() throws SQLException, ClassNotFoundException {
		super(RouteGeometry.class);
	}
	
	/**
	 * 
	 * @param aConnection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public RouteGeometryDao(HibernateConnection hibernateConnection) throws SQLException, ClassNotFoundException {
		super(RouteGeometry.class, hibernateConnection);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized RouteGeometry loadById(String id, String agencyName) {
		RouteGeometry rtn = super.loadById(id, agencyName);
		
		Hibernate.initialize(rtn.getAgency());
		return rtn;
	}
	
	public RouteGeometry findGeometryById(String id, String agencyName) throws DaoException
	{
		return RouteGeometry.class.cast(this.loadById(id, agencyName));
	}

}
