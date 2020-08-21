package me.math.grid.data;

import org.hashids.Hashids;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * @author markeverline
 *
 */
@JsonRootName(value = "DensityEstimateDataSample")
public class DensityEstimateDataSample extends AbstractDataSample {

	private double value = 0;
	
	/* (non-Javadoc)
	 * @see me.math.grid.data.AbstractDataSample#getValue()
	 */
	@Override
	@JsonGetter("value")
	public double getValue() {
		return this.value;
	}
	
	/**
	 * 
	 * @param value
	 */
	@JsonSetter("value")
	public void setValue(double value) {
		this.value = value;
	}

	/*
	 * 
	 */
	@Override
	@JsonIgnore
	public void addValue(double value) {
		this.value += value;
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	@Override
	public void copy(AbstractDataSample item) {
		if ( item instanceof DensityEstimateDataSample) {
			DensityEstimateDataSample obj = (DensityEstimateDataSample) item;
			obj.setInterpolationValue(this.getInterpolationValue());
			obj.setValue(this.getValue());
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
	            Double.hashCode(getValue())
	   );
	}


}
