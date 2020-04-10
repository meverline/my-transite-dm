/**
 * 
 */
package me.openMap.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import me.database.neo4j.IGraphDatabaseDAO;
import me.factory.DaoBeanFactory;
import me.openMap.OpenTransitMap;
import me.openMap.models.query.result.RouteQueryResult;
import me.openMap.models.query.result.TransitStopQueryResult;
import me.openMap.utils.StopOverlay;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;

/**
 * @author meverline
 *
 */
public class TransitStopDataHandler implements DataDisplayHandler {

	private List<TransitStop> data = null;
	
	public TransitStopDataHandler(List<TransitStop> query)
	{
		data = query;
	}
	
	/**
	 * @return the data
	 */
	public List<TransitStop> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<TransitStop> data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see me.openMap.handlers.DataHandler#processDataToGui(me.openMap.OpenTransitMap)
	 */
	@Override
	public void processDataToGui(OpenTransitMap gui) {
		
		if ( this.getData() == null || this.getData().isEmpty() ) { 
			return; 
		}
		
		List<RouteQueryResult> routeList = new ArrayList<RouteQueryResult>();
		List<TransitStopQueryResult> stopList = new ArrayList<TransitStopQueryResult>();
		
		HashMap<String,RouteQueryResult> routes = new HashMap<String,RouteQueryResult>();
		
		IGraphDatabaseDAO db =  IGraphDatabaseDAO.class.cast(DaoBeanFactory.create().getDaoBean( IGraphDatabaseDAO.class));
		
		for ( TransitStop stops : this.getData()) {
			if ( stops != null ) {
				List<RouteStopData> list = db.findRoutes(stops);
				for ( RouteStopData info : list) 
				{
					if ( ! routes.containsKey(info.getRouteShortName() ) ) {
						String headSign =  info.getTripHeadSign();
						if ( headSign.contains("\"")) {
							headSign = (headSign.replace('"', ' ')).trim();
						}
						RouteQueryResult result = new RouteQueryResult( info.getRouteShortName(),
																		headSign,	
																	    stops.getAgency());
						routes.put(info.getRouteShortName(), result);
						routeList.add( result);
					} else {
						
						String headSign =  info.getTripHeadSign();
						if ( headSign.contains("\"")) {
							headSign = (headSign.replace('"', ' ')).trim();
						}
						
						RouteQueryResult result = routes.get(info.getRouteShortName());
						
						if ( !  result.getTripHeadSign().contains(headSign)) {
							StringBuilder builder = new StringBuilder();
							builder.append(result.getTripHeadSign());
							builder.append(" / ");
							builder.append(headSign);
							result.setTripHeadSign(builder.toString());
						}
						
					}
				}
			}
			
			StopOverlay overlay = new StopOverlay(stops.getLocation(), 
												  stops.getName(), 
												  stops.getAgency());
			
			overlay.setDisplay( gui.isShowStops() );
			overlay.setAdaLimits( gui.isShowAdaStops() );
			
			stopList.add( new TransitStopQueryResult(stops, overlay));			
			gui.getMap().addOverlay( overlay );
		}
		
		Collections.sort(this.getData(), new StopSortComparable());
		Collections.sort(routeList, new RouteSortComparable());
				
		gui.setSearchResults( stopList, routeList);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	public class StopSortComparable implements Comparator<TransitStop> {

		@Override
		public int compare(TransitStop o1, TransitStop o2) {
			String lhs = "";
			String rhs = "";
			if ( o1.getDesc().length() > o1.getName().length() ) {
				lhs = o1.getDesc();
			} else {
				lhs = o1.getName();
			}
			
			if ( o2.getDesc().length() > o2.getName().length() ) {
				rhs = o2.getDesc();
			} else {
				rhs = o2.getName();
			}
			
			return lhs.compareTo(rhs);
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	public class RouteSortComparable implements Comparator<RouteQueryResult> {

		@Override
		public int compare(RouteQueryResult o1, RouteQueryResult o2) {
			return o1.getRouteNumber().compareTo(o2.getRouteNumber());
		}
	}


}
