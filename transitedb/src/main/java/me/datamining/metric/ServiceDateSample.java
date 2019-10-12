/**
 * 
 */
package me.datamining.metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.factory.DaoBeanFactory;
import me.transit.dao.DaoException;
import me.transit.dao.RouteDao;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.ServiceDate;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import me.datamining.metric.IDataProvider;

import me.datamining.metric.TransiteSpatialMetric;
/**
 * @author meverline
 *
 */
@JsonIgnoreProperties({ "daysOfIntrest", "routeSerive" })
public class ServiceDateSample extends TransiteSpatialMetric {
	

	private Set<ServiceDate.WeekDay> daysOfIntrest = new HashSet<ServiceDate.WeekDay>();
	private Map<String,List<ServiceDate.WeekDay>> routeSerive 
							= new HashMap<String,List<ServiceDate.WeekDay>>();
	private Log log = LogFactory.getLog(ServiceDateSample.class);
			
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
		
		RouteDao dao = RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(
				RouteDao.class));
		
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
