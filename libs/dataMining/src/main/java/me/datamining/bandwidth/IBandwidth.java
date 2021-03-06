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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = MesureOfSpread.class, name = "MesureOfSpread"),
		@Type(value = ScottsRule.class, name = "ScottsRule"),
		@Type(value = SlivermanRule.class, name = "SlivermanRule") })
public interface IBandwidth {

  /**
   * 
   * @param variance
   * @param dimensions
   * @param data
   * @return
   */
  public double bandWidth(double variance, int dimensions, DescriptiveStatistics data);
  
  /**
   * 
   * @param variance
   * @param dimensions
   * @param number
   * @return
   */
  public double bandWidth(double variance, int dimensions, long number);

}