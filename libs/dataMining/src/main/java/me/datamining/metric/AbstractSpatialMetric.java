package me.datamining.metric;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.apachecommons.CommonsLog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@CommonsLog
public abstract class AbstractSpatialMetric {

	@JsonIgnore
	private Map<Long, IDataProvider> dataList = new HashMap<Long,IDataProvider>();
	
	public abstract double getMetric(IDataProvider stop);
		
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
	protected Collection<IDataProvider> getDataList() {
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
	 * @see me.datamining.SpatialSamplePoint#addSampleData(java.lang.Object)
	 */
	public void addSampleData(Object data) {
		if ( data instanceof IDataProvider) {
			IDataProvider stop = IDataProvider.class.cast(data);
			
			if ( ! this.dataList.containsKey( stop.getUUID() )) {
				this.dataList.put(stop.getUUID(), stop);
			}
		}
	}
	


	
}
