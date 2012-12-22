/**
 * 
 */
package org.transiteRepositry.server.logic.impl;

import java.util.List;

import org.transiteRepositry.server.logic.StopQuery;
import org.transiteRepositry.server.request.CircleQueryRequest;
import org.transiteRepositry.server.request.TransiteStopQueryRequest;
import org.transiteRepositry.server.request.utils.Address;

import me.transit.dao.AgencyDao;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;

/**
 * @author meverline
 *
 */
public class StopQueryImpl implements StopQuery {

	private AgencyDao agencyDao = null;
	private TransiteStopDao transiteStopDao = null;
	
	/* (non-Javadoc)
	 * @see server.logic.StopQuery#query(me.transit.dao.query.StopQueryConstraint)
	 */
	/**
	 * @return the agencyDao
	 */
	public AgencyDao getAgencyDao() {
		return agencyDao;
	}

	/**
	 * @param agencyDao the agencyDao to set
	 */
	public void setAgencyDao(AgencyDao agencyDao) {
		this.agencyDao = agencyDao;
	}

	/**
	 * @return the transiteStopDao
	 */
	public TransiteStopDao getTransiteStopDao() {
		return transiteStopDao;
	}

	/**
	 * @param transiteStopDao the transiteStopDao to set
	 */
	public void setTransiteStopDao(TransiteStopDao transiteStopDao) {
		this.transiteStopDao = transiteStopDao;
	}

	@Override
	public List<TransitStop> query(StopQueryConstraint query) {
		return getTransiteStopDao().query(query);
	}

	/* (non-Javadoc)
	 * @see server.logic.StopQuery#query(server.request.CircleQueryRequest)
	 */
	@Override
	public List<TransitStop> query(CircleQueryRequest request) {
		StopQueryConstraint query = new StopQueryConstraint(); 
		
		if ( request.getAgency() != null && (! request.getAgency().isEmpty()) ) {
			query.addAgency( getAgencyDao().findByName(request.getAgency()));
		}
		
		for ( Address adr : request.getAddress()) {
			query.addCircleConstriant(adr.geoCode(), request.getDistance().toMeters());
		}
		
		return query(query);
	}

	/* (non-Javadoc)
	 * @see server.logic.StopQuery#query(server.request.TransiteStopQueryRequest)
	 */
	@Override
	public List<TransitStop> query(TransiteStopQueryRequest request) {
		StopQueryConstraint query = new StopQueryConstraint();
		
		if ( request.getAgency() != null && (! request.getAgency().isEmpty()) ) {
			query.addAgency( getAgencyDao().findByName(request.getAgency()));
		}
		
		query.addRectangleConstraint( request.getLatLonBox().getLowerLeft(),
									  request.getLatLonBox().getUpperRight());
		
		return query(query);
	}

}
