package org.transiteRepositry.server.logic;

import java.util.List;

import org.transiteRepositry.server.request.CircleQueryRequest;
import org.transiteRepositry.server.request.TransiteStopQueryRequest;


import me.transit.dao.query.StopQueryConstraint;
import me.transit.database.TransitStop;

public interface StopQuery {

	public List<TransitStop> query(StopQueryConstraint query);
	
	public List<TransitStop> query(CircleQueryRequest query);
	
	public List<TransitStop> query(TransiteStopQueryRequest query);
}
