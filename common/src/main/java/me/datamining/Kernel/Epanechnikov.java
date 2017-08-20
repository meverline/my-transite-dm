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

package me.datamining.Kernel;

public class Epanechnikov implements IDensityKernel {

	private final double squareRootOfFive = java.lang.Math.sqrt(5.0);

	/*
	 * (non-Javadoc)
	 * @see me.datamining.Kernel.IDensityKernel#kernelValue(double)
	 */
	public double kernelValue(double t) {

		if (Math.abs(t) < squareRootOfFive) {
			return ((3.0 / 4.0) * (1 - (1.0 / 5.0) * Math.pow(t, 2.0)))
					/ squareRootOfFive;
		} else {
			return 0;
		}
	}

}