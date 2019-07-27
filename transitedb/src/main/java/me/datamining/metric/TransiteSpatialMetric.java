package me.datamining.metric;


import java.util.ArrayList;
import java.util.List;

import me.database.mongo.DocumentDao;
import me.database.mongo.IDocument;
import me.database.mongo.IDocumentDao;
import me.database.neo4j.IGraphDatabaseDAO;
import me.factory.DaoBeanFactory;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;


public class TransiteSpatialMetric extends AbstractSpatialMetric {

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
		IGraphDatabaseDAO db = IGraphDatabaseDAO.class.cast(DaoBeanFactory.create().getDaoBean( IGraphDatabaseDAO.class));
		return db.findRoutes(stop);
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 */
	public List<Trip> getTrips(Route route) {
		
		List<Trip> rtn = null;

		IDocumentDao dao = IDocumentDao.class.cast(DaoBeanFactory.create().getDaoBean( IDocumentDao.class));
		
		List<String> fields = new ArrayList<String>();
		fields.add( Route.SHORTNAME);
		
		List<IQueryTuple> list = new ArrayList<IQueryTuple>();
		
		list.add( new StringTuple(DocumentDao.toDocField(fields), 
				                  route.getShortName(), 
				                  StringTuple.MATCH.EXACT ));
		
		List<IDocument> data = dao.find(list);
		
		rtn = Route.class.cast( data.get(0)).getTripList();
	
		return rtn;
	}

}
