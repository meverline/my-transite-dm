package me.datamining.sample;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.datamining.SpatialSamplePoint;
import me.math.grid.SpatialGridData;
import me.transit.database.TransitStop;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class AbstractSpatialSampleData extends SpatialGridData implements SpatialSamplePoint {

	@XStreamOmitField
	private Map<Long, TransitStop> dataList = new HashMap<Long,TransitStop>();
	@XStreamOmitField
	private boolean checked = false;
	
	protected abstract double getMetric(TransitStop stop);

	/*
	 * (non-Javadoc)
	 * @see me.math.grid.SpatialGridData#getValue()
	 */
	public double getValue() {
		return this.getSampleValue();
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getNumber()
	 */
	public double getNumber() {
		return getDataList().size();
	}

	/**
	 * @return the dataList
	 */
	protected Collection<TransitStop> getDataList() {
		return dataList.values();
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean isDataListEmpty()
	{
		return this.dataList.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#isChecked()
	 */
	public boolean isChecked() {
		return checked;
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#setChecked(boolean)
	 */
	public void setChecked(boolean flag) {
		this.checked = flag;
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#addSampleData(java.lang.Object)
	 */
	public void addSampleData(Object data) {
		if ( data instanceof TransitStop) {
			TransitStop stop = TransitStop.class.cast(data);
			
			if ( ! this.dataList.containsKey( stop.getUUID() )) {
				this.dataList.put(stop.getUUID(), stop);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see me.datamining.SpatialSamplePoint#getSampleValue()
	 */
	@Override
	public double getSampleValue() {
		double cnt = 0;
		for ( TransitStop stop : getDataList()) {
			cnt += getMetric(stop);
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
			min = Math.min(min, getMetric(stop));
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
			max = Math.max(max, getMetric(stop));
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
			cnt += getMetric(stop);
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
                total += Math.pow( getMetric(stop) - avg, 2 );
        }
        if ( getDataList().size() == 0 ) {
                return 0;
        }
        return Math.sqrt(total / getDataList().size());
	}

	
}
