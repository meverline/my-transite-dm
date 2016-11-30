/**
 * 
 */
package me.datamining.metric;

import me.datamining.metric.IDataProvider;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author meverline
 *
 */
@XStreamAlias("SpatialData")
public class DefaultSample extends AbstractSpatialMetric {
	
	public double getMetric(IDataProvider stop)
	{
		return 0;
	}
	
}
