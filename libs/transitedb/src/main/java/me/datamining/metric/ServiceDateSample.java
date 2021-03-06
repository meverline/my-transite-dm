/**
 * 
 */
package me.datamining.metric;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.apachecommons.CommonsLog;
import me.transit.dao.DaoException;
import me.transit.dao.RouteDao;
import me.transit.database.*;

import java.util.*;
/**
 * @author meverline
 *
 */
@JsonIgnoreProperties({ "daysOfIntrest", "routeSerive" })
@CommonsLog
public class ServiceDateSample extends TransiteSpatialMetric {

	private Set<ServiceDate.WeekDay> daysOfIntrest = new HashSet<ServiceDate.WeekDay>();
	private Map<String,List<ServiceDate.WeekDay>> routeSerive 
							= new HashMap<String,List<ServiceDate.WeekDay>>();

	/**
	 * @return the daysOfIntrest
	 */
	public Set<ServiceDate.WeekDay> getDaysOfIntrest() {
		return daysOfIntrest;
	}
	
	/**
	 * @param daysOfIntrest the daysOfIntrest to set
	 */
	public void setDaysOfIntrest(Set<ServiceDate.WeekDay> daysOfIntrest) {
		this.daysOfIntrest = daysOfIntrest;
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.datamining.sample.AbstractSpatialSampleData#getMetric(me.transit.database.TransitStop)
	 */
	public double getMetric(IDataProvider provider) {
		
		RouteDao dao = getDaoProvider().getRouteDao();
		
		TransitStop stop = TransitStop.class.cast(provider);

		int total = 0;
		List<RouteStopData> relations = this.getRoutes(stop);
		for (RouteStopData route : relations) {

			if (!routeSerive.containsKey(route.getRouteShortName())) {

				try {
					List<Route> list = dao.findByRouteNumber(route.getRouteShortName(),
												 			 stop.getAgency().getName());

					List<ServiceDate.WeekDay> aList = new ArrayList<ServiceDate.WeekDay>();

					for (Route rt : list) {
						List<Trip> tripList = this.getTrips(rt);
						for (Trip trip : tripList) {
							ServiceDate service = trip.getService();

							for (ServiceDate.WeekDay value : ServiceDate.WeekDay.values()) {
								if (service.hasService(value) && daysOfIntrest.contains(value)) {
									aList.add(value);
								}
							}
						}
					}
					
					routeSerive.put(route.getRouteShortName(), aList);
					
				} catch (DaoException e) {
					log.error(e);
				}
			}

			total += routeSerive.get(route.getRouteShortName()).size();
		}

		return total;
	}
	
}
