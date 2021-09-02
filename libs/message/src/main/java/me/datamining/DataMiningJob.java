package me.datamining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.datamining.shapes.Shape;
import me.datamining.types.DataMiningTypes;
import me.datamining.types.HourOfDay;
import me.datamining.types.MetricTypes;

@JsonRootName("DataMiningJob")
public class DataMiningJob {
	
	public enum Day { Sun, Mon, Tue, Wen, Thur, Fri, Sat }
	
	private String name;
	private double gridSpaceInMeters = 1609.34 / 4.0; // 1 mile
	private DataMiningTypes dataMiningType = DataMiningTypes.KDE_HEATMAP;
	private MetricTypes metricType = MetricTypes.ServiceFrequnceAtStop;
	private Shape shape;
	private HourOfDay startTime = null;
	private HourOfDay endTime = null;
	private List<String> agencies  = new ArrayList<>();
	private List<Day> weekdays = new ArrayList<>();
	
	
	public DataMiningJob() {
	}
	
	/**
	 * 
	 * @param name the name
	 * @param shape the shape
	 */
	public DataMiningJob(String name, Shape shape) {
		this.setName(name);
		this.setShape(shape);
	}


	/**
	 * @return the name
	 */
	@JsonGetter("name")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dataMiningType default is KDE_HEATMAP
	 */
	@JsonGetter("dataMiningType")
	public DataMiningTypes getDataMiningType() {
		return dataMiningType;
	}

	/**
	 * @param dataMiningType the dataMiningType to set
	 */
	@JsonSetter("dataMiningType")
	public void setDataMiningType(DataMiningTypes dataMiningType) {
		this.dataMiningType = dataMiningType;
	}

	/**
	 * @return the shape
	 */
	@JsonGetter("shape")
	public Shape getShape() {
		return shape;
	}

	/**
	 * @param shape the shape to set
	 */
	@JsonSetter("shape")
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	/**
	 * @return the agencies
	 */
	@JsonGetter("agencies")
	public List<String> getAgencies() {
		return agencies;
	}

	/**
	 * @param agencies the agencies to set
	 */
	@JsonSetter("agencies")
	public void setAgencies(List<String> agencies) {
		this.agencies = agencies;
	}

	/**
	 * @return the startTime
	 */
	@JsonGetter("startTime")
	public HourOfDay getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	@JsonSetter("startTime")
	public void setStartTime(HourOfDay startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * @param startTime the startTime to set
	 */
	@JsonIgnore
	public void setStartTime(Calendar startTime) {
		this.startTime = new HourOfDay(startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE));
	}

	/**
	 * @return the endTime
	 */
	@JsonGetter("endTime")
	public HourOfDay getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	@JsonSetter("endTime")
	public void setEndTime(HourOfDay endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @param endTime the startTime to set
	 */
	@JsonIgnore
	public void setEndTime(Calendar endTime) {
		this.endTime = new HourOfDay(endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE));
	}

	/**
	 * @return the weekdays
	 */
	@JsonGetter("weekdays")
	public List<Day> getWeekdays() {
		return weekdays;
	}

	/**
	 * @param weekdays the weekdays to set
	 */
	@JsonSetter("weekdays")
	public void setWeekdays(List<Day> weekdays) {
		this.weekdays = weekdays;
	}

	/**
	 * @return the metricType
	 */
	@JsonGetter("metricType")
	public MetricTypes getMetricType() {
		return metricType;
	}

	/**
	 * @param metricType the metricType to set default is ServiceFrequnceAtStop
	 */
	@JsonSetter("metricType")
	public void setMetricType(MetricTypes metricType) {
		this.metricType = metricType;
	}

	/**
	 * @return the gridSpaceInMeters
	 */
	@JsonGetter("gridSpaceInMeters")
	public double getGridSpaceInMeters() {
		return gridSpaceInMeters;
	}

	/**
	 * @param gridSpaceInMeters the gridSpaceInMeters to set, default is 1063.3 m or 1 mile
	 */
	@JsonSetter("gridSpaceInMeters")
	public void setGridSpaceInMeters(double gridSpaceInMeters) {
		this.gridSpaceInMeters = gridSpaceInMeters;
	}
	
}
