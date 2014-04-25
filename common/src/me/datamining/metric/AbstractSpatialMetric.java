package me.datamining.metric;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.database.mongo.DocumentDao;
import me.transit.dao.neo4j.GraphDatabaseDAO;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;
import me.transit.parser.TransitFeedParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class AbstractSpatialMetric {

	@XStreamOmitField
	public static Log log = LogFactory.getLog(TransitFeedParser.class);
	@XStreamOmitField
	private Map<Long, TransitStop> dataList = new HashMap<Long,TransitStop>();
	
	public abstract double getMetric(TransitStop stop);
		
	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getNumber()
	 */
	public double getNumber() {
		return getDataList().size();
	}

	/**
	 * @return the dataList
	 */
	protected Collection<TransitStop> getDataList() {
		return dataList.values();
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean isDataListEmpty()
	{
		return this.dataList.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#addSampleData(java.lang.Object)
	 */
	public void addSampleData(Object data) {
		if ( data instanceof TransitStop) {
			TransitStop stop = TransitStop.class.cast(data);
			
			if ( ! this.dataList.containsKey( stop.getUUID() )) {
				this.dataList.put(stop.getUUID(), stop);
			}
		}
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
