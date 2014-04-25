package me.datamining.metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.factory.DaoBeanFactory;
import me.transit.dao.DaoException;
import me.transit.dao.RouteDao;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.ServiceDate;
import me.transit.database.StopTime;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class ServiceFrequnceAtStop extends AbstractSpatialMetric {

	@XStreamOmitField
	private Set<ServiceDate.WeekDay> daysOfIntrest = new HashSet<ServiceDate.WeekDay>();
	
	@XStreamOmitField
	private long startTime = 0;
	
	@XStreamOmitField
	private long endTime = 235959;
	
	@XStreamOmitField
	private Log log = LogFactory.getLog(ServiceDateSample.class);
	
	private HashMap<String,Integer> stopValue = new HashMap<String, Integer>();
	
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
	
	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	private boolean isScheduleOfIntrest(ServiceDate service )
	{
		for (ServiceDate.WeekDay value : ServiceDate.WeekDay.values()) {
			if (service.hasService(value) && daysOfIntrest.contains(value)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param stop
	 * @param schedule
	 * @return
	 */
	private List<StopTime> findStopTimes(TransitStop stop, List<StopTime> schedule)
	{
		List<StopTime> rtn = new ArrayList<StopTime>();
		
		for (StopTime st : schedule) {
			if (st.getStopId() == stop.getId()) {

				for (Long time : st.getArrivalTime()) {
					if (time > this.getStartTime() && time < this.getEndTime()) {
						rtn.add(st);
					}
				}

			}
		}
		return rtn;		
	}

	/**
	 * 
	 * @param stop
	 * @return
	 */
	public double getMetric(TransitStop stop) {
		
		RouteDao dao = RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(
											RouteDao.class));

		if ( stopValue.containsKey(stop.getId())) {
			return stopValue.get(stop.getId()).intValue();
		}
		
		List<StopTime> stopTimes = new ArrayList<StopTime>();
		List<RouteStopData>  relations = this.getRoutes(stop);
		for (RouteStopData route : relations) {

			try {
				List<Route> list = dao.findByRouteNumber(route.getRouteShortName(), 
														 stop.getAgency().getName());

				Route rt = list.get(0);
				List<Trip> tripList = this.getTrips(rt);
				for (Trip trip : tripList) {
					if (isScheduleOfIntrest(trip.getService())) {
						List<StopTime> schedule = this.findStopTimes(stop,
																	 trip.getStopTimes());
						if (!schedule.isEmpty()) {
							stopTimes.addAll(schedule);
						}
					}
				}
			} catch (DaoException e) {
				log.error(e);
			}
		}
		
		stopValue.put(stop.getId(), stopTimes.size());
		return stopValue.get(stop.getId()).intValue();
	}

}
