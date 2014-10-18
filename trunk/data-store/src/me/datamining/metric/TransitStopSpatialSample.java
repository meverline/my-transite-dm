package me.datamining.metric;

import java.util.List;

import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The TransitStopSpatialSample: Value returned is the sum of the number of 
 * routes that stop at the stops:
 *
 * @author meverline
 *
 */
@XStreamAlias("SpatialData")
public class TransitStopSpatialSample extends AbstractSpatialMetric {
		
	public double getMetric(TransitStop stop)
	{
		List<RouteStopData>  relations = this.getRoutes(stop);
		if ( stop != null && relations!= null )  {
			return relations.size();
		}
		return 0.0;
	}

}
