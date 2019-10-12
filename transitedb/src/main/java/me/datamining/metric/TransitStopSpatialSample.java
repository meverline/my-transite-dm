package me.datamining.metric;

import java.util.List;

import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.datamining.metric.TransiteSpatialMetric;

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
