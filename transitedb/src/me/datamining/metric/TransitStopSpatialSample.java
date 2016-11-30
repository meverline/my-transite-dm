package me.datamining.metric;

import java.util.List;

import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import me.datamining.metric.TransiteSpatialMetric;
/**
 * The TransitStopSpatialSample: Value returned is the sum of the number of 
 * routes that stop at the stops:
 *
 * @author meverline
 *
 */
@XStreamAlias("SpatialData")
public class TransitStopSpatialSample extends TransiteSpatialMetric {
		
	public double getMetric(IDataProvider stop)
	{
		List<RouteStopData>  relations = this.getRoutes(TransitStop.class.cast(stop));
		if ( stop != null && relations!= null )  {
			return relations.size();
		}
		return 0.0;
	}

}
