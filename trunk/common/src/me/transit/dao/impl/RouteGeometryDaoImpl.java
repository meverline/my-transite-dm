package me.transit.dao.impl;

import java.sql.SQLException;

import me.transit.dao.DaoException;
import me.transit.dao.RouteGeometryDao;
import me.transit.database.RouteGeometry;

import org.hibernate.Hibernate;

public class RouteGeometryDaoImpl extends TransitDaoImpl implements RouteGeometryDao {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public RouteGeometryDaoImpl() throws SQLException, ClassNotFoundException {
		super(RouteGeometry.class);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.TransitDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized Object loadById(String id, String agencyName) {
		RouteGeometry rtn = RouteGeometry.class.cast(super.loadById(id, agencyName));
		
		Hibernate.initialize(rtn.getAgency());
		return rtn;
	}
	
	public RouteGeometry findGeometryById(String id, String agencyName) throws DaoException
	{
		return RouteGeometry.class.cast(this.loadById(id, agencyName));
	}

}
