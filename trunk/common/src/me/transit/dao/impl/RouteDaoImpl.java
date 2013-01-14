package me.transit.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.transit.dao.DaoException;
import me.transit.dao.RouteDao;
import me.transit.database.Route;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class RouteDaoImpl extends TransitDaoImpl<Route> implements RouteDao {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public RouteDaoImpl() throws SQLException, ClassNotFoundException {
		super(Route.class);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.RouteDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized Route loadById(String id, String agencyName) {
		Route rtn = super.loadById(id, agencyName);
		
		if ( rtn != null ) {
			Hibernate.initialize(rtn.getAgency());
			Hibernate.initialize(rtn.getTripList());
		}
		return rtn;
	}

	/* (non-Javadoc)
	 * @see me.transit.dao.impl.RouteDao#findByRouteNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Route> findByRouteNumber(String routeNumber, String agencyName) throws DaoException
	{
		List<Route> rtn = new ArrayList<Route>();
		
		try {

			Session session = getSession();
			Criteria crit = session.createCriteria(Route.class);
			
			crit.add(Restrictions.like("shortName", routeNumber));
			crit.createAlias("agency", "agency").add(Restrictions.eq("agency.name", 
			
					agencyName));
			
			for ( Object obj : crit.list()) {
				Route rt = Route.class.cast(obj);
				
				Hibernate.initialize(rt.getAgency());
				Hibernate.initialize(rt.getTripList());
				
				rtn.add(rt);
			}
			session.close();
		
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
			throw new DaoException("Route: " + routeNumber + " Agency: " + agencyName, ex);
		}

		return rtn;
	}
		
}
