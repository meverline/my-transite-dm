package me.datamining;

import me.math.Vertex;

public interface SpatialSamplePoint {
	
	/**
	 * The latitude in Degress of Point.
	 * @return
	 */
	public double getLatitudeDegress();

	/**
	 * The longitude in Degress of Point.
	 * @return
	 */
	public double getLongitudeDegress();
	
	/**
	 * The Value which represtents a give sample
	 * value at a give point. 	
	 * @return
	 */
	public double getSampleValue();
	
	/**
	 * The number of items that go into the value.
	 * @return
	 */
	public double getValue();
	
	/**
	 * Convert point to math vertext type.
	 * @return
	 */
	public Vertex toVertex();
	
	/**
	 * Return the min value of the data point.
	 * @return
	 */
	public double getMin();
	
	/**
	 * Return the max value of the data point.
	 * @return
	 */
	public double getMax();
	
    /**
     * Get the nubmer of sample points at this location. 
     * @return
     */
    public double getNumber();
    
    /**
     * Get the average of the sample points.
     * @return
     */
    public double average();
    
    /**
     * Get the standard deviation of the sample points.
     * @return
     */
    public double standardDeviation();
    
	/**
	 * For dataminig algorthim's has the point been checked already.
	 * @return
	 */
	public boolean isChecked();

	/**
	 * For dataminning algorthims set if the data point is already set.
	 * @param flag
	 */
	public void setChecked(boolean flag);
	
	
	/**
	 * 
	 * @param flag
	 */
	public void addSampleData(Object data);

}
