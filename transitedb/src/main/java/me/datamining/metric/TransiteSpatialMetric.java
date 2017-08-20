package me.datamining.metric;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import me.database.mongo.DocumentDao;
import me.datamining.metric.AbstractSpatialMetric;
import me.datamining.metric.IDataProvider;
import me.transit.dao.neo4j.GraphDatabaseDAO;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;


public class TransiteSpatialMetric extends AbstractSpatialMetric {

	@Override
	public double getMetric(IDataProvider stop) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 
	 * @param stop
	 * @return
	 */
	public List<RouteStopData> getRoutes(TransitStop stop) {
		GraphDatabaseDAO db = GraphDatabaseDAO.instance();
		return db.findRoutes(stop);
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 */
	public List<Trip> getTrips(Route route) {
		
		List<Trip> rtn = null;
		try {
			DocumentDao dao = DocumentDao.instance();
			
			List<String> fields = new ArrayList<String>();
			fields.add( Route.SHORTNAME);
			
			List<IQueryTuple> list = new ArrayList<IQueryTuple>();
			
			list.add( new StringTuple(DocumentDao.toDocField(fields), 
					                  route.getShortName(), 
					                  StringTuple.MATCH.EXACT ));
			
			List<Object> data = dao.find(list);
			
			rtn = Route.class.cast( data.get(0)).getTripList();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtn;
	}

}
