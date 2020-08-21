package me.math.grid.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.hashids.Hashids;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonRootName(value = "STINGDataSample")
public class STINGDataSample extends AbstractDataSample {

	private boolean checked = false;
	private List<Double> values = new ArrayList<>();
	private transient  DescriptiveStatistics stats = new DescriptiveStatistics();
	
	/**
	 * 
	 * @return
	 */
	@JsonIgnore
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
	@JsonIgnore
	public double getValue() {
		return stats.getSum();
	}
	
	@Override
	@JsonIgnore
	public void addValue(double value) {
		this.values.add(value);
		stats.addValue(value);
	}
	
	@JsonGetter("values")
	public List<Double> getValues() {
		return values;
	}
	
	@JsonSetter("values")
	public void setValues(List<Double> values) {
		stats.clear();
		this.values.addAll(values);
		this.values.forEach( item -> stats.addValue(item));
	}
	
	@JsonIgnore
	public double getSampleNumber()
	{
		return stats.getN();
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public double getMin() {
		return stats.getMin();
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public double getMax() {
		return stats.getMax();
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public double average() {
		return stats.getMean();
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public double standardDeviation() {
		return stats.getStandardDeviation();
	}
	
	/*
	 * 
	 */
	@Override
	public void copy(AbstractDataSample item) {
		if ( item instanceof STINGDataSample) {
			STINGDataSample obj = (STINGDataSample) item;
			obj.setInterpolationValue(this.getInterpolationValue());
			obj.setChecked(this.isChecked());
			obj.setValues(this.getValues());
		}		
	}
	
	/*
	 * 
	 */
	@Override
	public String hash() {
	   Hashids hashids = new Hashids(getClass().getName());
	   return hashids.encode(
	            Double.hashCode(getInterpolationValue()),
	            Double.hashCode(getValue()),
	            values.size(),
	            Double.hashCode(average()),
	            Double.hashCode(standardDeviation()),
	            Double.hashCode(getMin()),
	            Double.hashCode(getMin())
	   );
	}

}
