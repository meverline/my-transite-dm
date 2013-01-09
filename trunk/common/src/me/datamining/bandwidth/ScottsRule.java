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

public class ScottsRule implements IBandwidth {

	/*
	 * (non-Javadoc)
	 * @see me.datamining.bandwidth.IBandwidth#bandWidth(double, int, org.apache.commons.math.stat.descriptive.DescriptiveStatistics)
	 */
	public double bandWidth(double variance, int dimensions, DescriptiveStatistics data) {

		double pow = -1.0 / (dimensions + 4);
		return variance * Math.pow(data.getN(), pow);
	}

}