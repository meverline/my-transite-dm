/**
 * 
 */
package me.datamining.metric;

import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author meverline
 *
 */
@XStreamAlias("SpatialData")
public class DefaultSample extends AbstractSpatialMetric {
	
	public double getMetric(TransitStop stop)
	{
		return 0;
	}
	
}
