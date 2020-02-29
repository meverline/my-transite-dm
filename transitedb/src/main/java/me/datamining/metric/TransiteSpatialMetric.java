package me.datamining.metric;


import java.util.ArrayList;
import java.util.List;

import me.database.mongo.DocumentDao;
import me.database.mongo.IDocument;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Route;
import me.transit.database.RouteDocument;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;


public class TransiteSpatialMetric extends AbstractSpatialMetric {
	
	private DaoProvider daoProvider;
	
	/**
	 * @return the daoProvider
	 */
	public DaoProvider getDaoProvider() {
		return daoProvider;
	}

	/**
	 * @param daoProvider the daoProvider to set
	 */
	public void setDaoProvider(DaoProvider daoProvider) {
		this.daoProvider = daoProvider;
	}

	@Override
	public double getMetric(IDataProvider stop) {
		return 0;
	}
	
	/**
	 * 
	 * @param stop
	 * @return
	 */
	public List<RouteStopData> getRoutes(TransitStop stop) {
		return this.getDaoProvider().getGraphDatabase().findRoutes(stop);
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 */
	public List<Trip> getTrips(Route route) {
		
		List<Trip> rtn = null;
		
		List<String> fields = new ArrayList<String>();
		fields.add( Route.SHORTNAME);
		
		List<IQueryTuple> list = new ArrayList<IQueryTuple>();
		
		list.add( new StringTuple(DocumentDao.toDocField(fields), 
				                  route.getShortName(), 
				                  StringTuple.MATCH.EXACT ));
		
		List<IDocument> data = getDaoProvider().getDocumentDao().find(list);
		
		rtn = RouteDocument.class.cast( data.get(0)).getTrips();
	
		return rtn;
	}

}
