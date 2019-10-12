package me.math.grid.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonRootName(value = "STINGDataSample")
public class STINGDataSample extends AbstractDataSample {


	private boolean checked = false;
	private List<Double> values = new ArrayList<Double>();
	private transient  DescriptiveStatistics stats = null;
	
	private void init()
	{
		if ( stats == null) {
			stats = new DescriptiveStatistics();
		}
	}
	/**
	 * 
	 * @return
	 */
	protected boolean isDataListEmpty()
	{
		return this.values.isEmpty();
	}

	/**
	 * 
	 * @return
	 */
	@JsonGetter("checked")
	public boolean isChecked() {
		return checked;
	}
	
	/**
	 * 
	 * @param flag
	 */
	@JsonSetter("checked")
	public void setChecked(boolean flag) {
		this.checked = flag;
	}

	
	@Override
	public double getValue() {
		init();
		return stats.getSum();
	}

	@Override
	public void addValue(double value) {
		init();
		this.values.add(value);
		stats.addValue(value);
	}
	
	@JsonGetter("values")
	public List<Double> getValues() {
		return values;
	}
	
	@JsonSetter("values")
	public void setValues(List<Double> values) {
		this.values = values;
	}
	
	public double getSampleNumber()
	{
		init();
		return stats.getN();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMin() {
		init();
		return stats.getMin();
	}

	/**
	 * 
	 * @return
	 */
	public double getMax() {
		init();
		return stats.getMax();
	}

	/**
	 * 
	 * @return
	 */
	public double average() {
		init();
		return stats.getMean();
	}

	/**
	 * 
	 * @return
	 */
	public double standardDeviation() {
		init();
		return stats.getStandardDeviation();
	}

}
