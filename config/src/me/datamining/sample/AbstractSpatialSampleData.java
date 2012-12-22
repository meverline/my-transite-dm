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
	
}
