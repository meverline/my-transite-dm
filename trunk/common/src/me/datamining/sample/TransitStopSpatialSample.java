package me.datamining.sample;

import java.util.List;

import me.transit.database.TransitStop;

import org.neo4j.graphdb.Node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * The TransitStopSpatialSample: Value returned is the sum of the number of 
 * routes that stop at the stops:
 *
 * @author meverline
 *
 */
@XStreamAlias("SpatialData")
public class TransitStopSpatialSample extends AbstractSpatialSampleData {
	
	@XStreamAlias("value")
	@XStreamConverter(me.database.TransitStopDataSampleConverter.class)
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
	
	protected double getMetric(TransitStop stop)
	{
		List<Node> relations = this.getRoutes(stop);
		if ( stop != null && relations!= null )  {
			return relations.size();
		}
		return 0.0;
	}

}
