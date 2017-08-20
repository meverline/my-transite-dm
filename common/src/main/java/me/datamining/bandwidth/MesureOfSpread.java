//CIRAS: Crime Information Retrieval and Analysis System
//Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.datamining.bandwidth;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class MesureOfSpread implements IBandwidth {

	private double q1_ = 25.0;
	private double q3_ = 75.0;

	/*
	 * 
	 */
	public MesureOfSpread() {
	}

	/**
	 * 
	 * @param q1
	 * @param q3
	 */
	public MesureOfSpread(double q1, double q3) {
		q1_ = q1;
		q3_ = q3;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setQuartileLow(double value)
	{
		q1_ = value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setQuartileHigh(double value)
	{
		q3_ = value;
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.bandwidth.IBandwidth#bandWidth(double, int, org.apache.commons.math.stat.descriptive.DescriptiveStatistics)
	 */
	public double bandWidth(double variance, int dimensions,
			DescriptiveStatistics data) {

		double q1 = data.getPercentile(q1_);
		double q3 = data.getPercentile(q3_);
		return 0.9 * Math.min(variance, (q3 - q1) / 1.34)
							* Math.pow(data.getN(), -(1.0 / 5.0));
	}

	/*
	 * (non-Javadoc)
	 * @see me.datamining.bandwidth.IBandwidth#bandWidth(double, int, long)
	 */
	public double bandWidth(double variance, int dimensions, long number) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	

}