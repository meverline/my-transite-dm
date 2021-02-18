package me.transit.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.transit.database.Route;

@Repository(value="routeDao")
@SuppressWarnings("deprecation")
@Scope("singleton")
@Transactional
public class RouteDao extends TransitDao<Route>  {

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Autowired
	public RouteDao(SessionFactory aSessionFactory) throws SQLException, ClassNotFoundException {
		super(Route.class, aSessionFactory);
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.impl.RouteDao#findByRouteNumber(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Route> findByRouteNumber(String routeNumber, String agencyName) throws DaoException
	{
		List<Route> rtn;

		try {
			Session session = getSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Route> crit = builder.createQuery(Route.class);
			
			Root<Route> root = crit.from(Route.class);
			
			crit.where(
					builder.like(root.get("shortName"), routeNumber),
					builder.equal(root.get("agency").get("name"), agencyName)
			);
			
			rtn = session.createQuery(crit).getResultList();
			
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
	public RouteListIterator listAllRoutes()
	{		
		Session session = getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Route> crit = builder.createQuery(Route.class);
		Root<Route> root = crit.from(Route.class);
		crit.orderBy(builder.desc(root.get("shortName")));
		
		// query.setReadOnly(true);
		javax.persistence.Query query = session.createQuery(crit);
		@SuppressWarnings("rawtypes")
		org.hibernate.Query hquery = query.unwrap(org.hibernate.Query.class);
		ScrollableResults results = hquery.scroll(ScrollMode.FORWARD_ONLY);
	    	    
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
			this._session = null;
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
