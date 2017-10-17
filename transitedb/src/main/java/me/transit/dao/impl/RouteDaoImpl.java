package me.transit.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import me.database.hibernate.HibernateConnection;
import me.transit.dao.DaoException;
import me.transit.dao.RouteDao;
import me.transit.database.Route;

@SuppressWarnings("deprecation")
public class RouteDaoImpl extends TransitDaoImpl<Route> implements RouteDao {
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public RouteDaoImpl() throws SQLException, ClassNotFoundException {
		super(Route.class);
	}
	
	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public RouteDaoImpl(HibernateConnection aConnection) throws SQLException, ClassNotFoundException {
		super(Route.class, aConnection);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.RouteDao#loadById(long, java.lang.String)
	 */
	@Override
	public synchronized Route loadById(String id, String agencyName) {
		Route rtn = super.loadById(id, agencyName);
		
		if ( rtn != null ) {
			Hibernate.initialize(rtn.getAgency());
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
				rtn.add(rt);
			}
			session.close();
		
		} catch (HibernateException ex) {
			getLog().error(ex.getLocalizedMessage(), ex);
			throw new DaoException("Route: " + routeNumber + " Agency: " + agencyName, ex);
		}

		return rtn;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public RouteListIterator listAllRoutes()
	{		
		Session session = getSession();
		Query<Route> query = session.createQuery("from Route as urc order by shortName");

		// query.setReadOnly(true);
	    ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
	    	    
	    return new RouteListIterator(results, session);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	
	public class RouteListIterator {
		
		private final static int BUFFER_SIZE = 200;
		private ScrollableResults _results = null;
		private List<Route> _bufferList = new ArrayList<Route>();
		private Session _session = null;
		
		/**
		 * 
		 * @param results
		 * @param session
		 */
		public RouteListIterator(ScrollableResults results, Session session)
		{
			this._results = results;
			this._session = session;
		}
		
		/**
		 * 
		 */
		public void finalize()
		{
			this.done();
		}
		
		/**
		 * 
		 */
		public void done()
		{
			if ( this._results != null ) {
				this._results.close();
				this._results = null;
			}
			if ( this._session != null ) {
				this._session.close();
				this._session = null;
			}
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean hasNext() {
		
			if ( this._bufferList.size() > 0 ) {
				return true;
			}  
			if ( this._results == null ) {
				return false;
			}
			return (! this._results.isLast());
		}
		
		/**
		 * 
		 * @return
		 */
		public Route next() {
	
			if ( this._bufferList.size() < 1 && this._results != null) {				
				int ndx = 0; 
				while ( this._results.next() && ndx < RouteListIterator.BUFFER_SIZE ) {
					Route rt = Route.class.cast(_results.get()[0]);
					
					Hibernate.initialize(rt.getAgency());					
					this._bufferList.add(rt);
					ndx++;
				}
				
				if ( this._results.isLast()) {
					this.done();
				}	
			}
			
			Route rtn = null;
			if ( this._bufferList.size() > 0 ) {
			  rtn =  this._bufferList.remove(0);
			}
			return rtn;
		}
			
	}
		
}
