package me.transit.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.transit.database.RouteGeometry;

@Repository(value="routeGeometryDao")
@Scope("singleton")
@Transactional
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
