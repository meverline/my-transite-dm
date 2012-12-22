/**
 * 
 */
package me.datamining.sample;

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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author meverline
 *
 */
@XStreamAlias("SpatialData")
public class ServiceDateSample extends AbstractSpatialSampleData {
	
	@XStreamOmitField
	private Set<ServiceDate.WeekDay> daysOfIntrest = new HashSet<ServiceDate.WeekDay>();
	@XStreamOmitField
	private Map<String,List<ServiceDate.WeekDay>> routeSerive 
							= new HashMap<String,List<ServiceDate.WeekDay>>();
	@XStreamOmitField
	private Log log = LogFactory.getLog(ServiceDateSample.class);
	
	@XStreamAlias("value")
	@XStreamConverter(me.database.ServiceDateSampleDataValueConverter.class)
	private AbstractSpatialSampleData self = this;

	/**
	 * @return the self
	 */
	public AbstractSpatialSampleData getSelf() {
		return self;
	}

	/**
	 * @param self the self to set
	 */
	public void setSelf(AbstractSpatialSampleData self) {
		this.self = self;
	}

		
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
	 * 
	 * @param stop
	 * @return
	 */
	private int getServiceAmount(TransitStop stop) {
		
		RouteDao dao = RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(
				RouteDao.class));

		int total = 0;
		for (RouteStopData route : stop.getRoutes()) {

			if (!routeSerive.containsKey(route.getRouteShortName())) {

				try {
					List<Route> list = dao.findByRouteNumber(route.getRouteShortName(),
												 			 stop.getAgency().getName());

					List<ServiceDate.WeekDay> aList = new ArrayList<ServiceDate.WeekDay>();

					for (Route rt : list) {
						for (Trip trip : rt.getTripList()) {
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

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getSampleValue()
	 */
	@Override
	public double getSampleValue() {
		double cnt = 0;
		for ( TransitStop stop : getDataList()) {
			cnt += getServiceAmount(stop);
		}
		return cnt;
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getMin()
	 */
	@Override
	public double getMin() {
		double min = 0;
		for ( TransitStop stop : getDataList()) {
			min = Math.min(min, getServiceAmount(stop));
		}
		return min;
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getMax()
	 */
	@Override
	public double getMax() {
		double max = 0;
		for ( TransitStop stop : getDataList()) {
			max = Math.max(max, getServiceAmount(stop));
		}
		return max;
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#average()
	 */
	@Override
	public double average() {
		double cnt = 0;
		for ( TransitStop stop : getDataList()) {
			cnt += getServiceAmount(stop);
		}
		return cnt/this.getNumber();
	}

	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#standardDeviation()
	 */
	@Override
	public double standardDeviation() {
        double avg = average();
        double total  = 0.0;

        for ( TransitStop stop : getDataList()) {
                total += Math.pow( getServiceAmount(stop) - avg, 2 );
        }
        if ( getDataList().size() == 0 ) {
                return 0;
        }
        return Math.sqrt(total / getDataList().size());
	}

}
