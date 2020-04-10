package me.datamining.metric;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractSpatialMetric {

	@JsonIgnore
	public static Log log = LogFactory.getLog(AbstractSpatialMetric.class);
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
