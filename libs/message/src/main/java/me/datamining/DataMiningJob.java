package me.datamining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import me.datamining.shapes.Shape;
import me.datamining.types.DataMiningTypes;
import me.datamining.types.HourOfDay;
import me.datamining.types.MetricTypes;

@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("DataMiningJob")
public class DataMiningJob {
	
	public enum Day { SUNDAY, MONDAY, TUESDAY, WENSDAY, THURSDAY, 	FRIDAY, SATURDAY }
	
	private String name;
	private double gridSpaceInMeters = 1609.34 / 4.0; // 1 mile
	private DataMiningTypes dataMiningType = DataMiningTypes.KDE_HEATMAP;
	private MetricTypes metricType = MetricTypes.ServiceFrequencyAtStop;
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
	 * @param startTime the startTime to set
	 */
	@JsonIgnore
	public void setStartTime(Calendar startTime) {
		this.startTime = new HourOfDay(startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE));
	}

	/**
	 * @param endTime the startTime to set
	 */
	@JsonIgnore
	public void setEndTime(Calendar endTime) {
		this.endTime = new HourOfDay(endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE));
	}

}
