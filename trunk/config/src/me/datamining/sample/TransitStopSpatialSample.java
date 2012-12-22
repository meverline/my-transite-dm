package me.datamining.sample;

import java.util.Collection;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import me.transit.database.TransitStop;

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

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getSampleValue()
	 */
	public double getSampleValue() {
		double cnt = 0.0;
		
		Collection<TransitStop> aList = getDataList();
		for ( TransitStop stop : aList) {
			if ( stop != null && stop.getRoutes() != null ) {
				cnt += stop.getRoutes().size();
			}
		}
		return cnt;
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getMin()
	 */
	public double getMin() {
		double min = Double.MAX_VALUE;
		
		for ( TransitStop stop : getDataList()) {
			min = Math.min(min, stop.getRoutes().size());
		}
		return min;
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getMax()
	 */
	public double getMax() {
		double max = Double.MIN_VALUE;
		
		for ( TransitStop stop : getDataList()) {
			max = Math.max(max, stop.getRoutes().size());
		}
		return max;
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#average()
	 */
	public double average() {
		double cnt = 0.0;
		
		for ( TransitStop stop : getDataList()) {
			cnt += stop.getRoutes().size();
		}
		return cnt/this.getNumber();
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#standardDeviation()
	 */
	public double standardDeviation() {
        double avg = average();
        double total  = 0.0;

        for ( TransitStop stop : getDataList()) {
                total += Math.pow( stop.getRoutes().size() - avg, 2 );
        }
        if ( getDataList().size() == 0 ) {
                return 0;
        }
        return Math.sqrt(total / getDataList().size());

	}

}
