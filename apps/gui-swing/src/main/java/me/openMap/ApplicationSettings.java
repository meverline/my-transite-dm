//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.openMap;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.utils.ColorGradient;
import me.utils.GradientParameters;

@JsonRootName(value = "ApplicationSettings")
public class ApplicationSettings {
	
	
    private static ApplicationSettings theOne = null;
	
	private HashMap<String,Color> agencyColorMap = new HashMap<String,Color>();
    private List<GradientParameters> gradParms = new ArrayList<GradientParameters>();
 
   
    private Color addRangeColor = Color.blue;
    private int addAlphaValue = 20;
    private int clusterHiRange = Integer.MAX_VALUE;
    private int clusterLowRange = 0;
    private double clusterConfidence = 0.5;
    private double clusterDensity = 1;
    private IBandwidth xbandWidth = null;
    private IBandwidth ybandWidth = null;
    private IDensityKernel denstiyKernal = null;
    
    private List<ColorGradient> gradients = new ArrayList<ColorGradient>();
    
    /**
     * 
     */
	private ApplicationSettings()
    { 
		gradParms.add(new GradientParameters(Color.decode("#A0A0FF"), Color.BLUE, 0.1, 29.9, 15, 95));
		gradParms.add(new GradientParameters(Color.BLUE, Color.GREEN, 30, 29.9,10, 95));
		gradParms.add(new GradientParameters(Color.GREEN, Color.YELLOW, 60.0,19.9, 10, 95));
		gradParms.add(new GradientParameters(Color.YELLOW, Color.decode("#FF0000"), 80.0, 9.9, 10, 95));
		gradParms.add(new GradientParameters(Color.decode("#FF0000"), Color.RED,90.0, 10.0, 10, 95));
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static ApplicationSettings create()
	{
		if ( theOne == null ) {
			theOne = ApplicationSettings.load();
			if ( theOne == null ) {
			   theOne = new ApplicationSettings();
			}
		}
		return theOne;
	}
	 
	/**
	 * 
	 * @param url
	 * @return
	 */
	public Color getAgencyColorByUrl(String url) {
		if ( agencyColorMap.containsKey(url) ) {
		   return agencyColorMap.get(url);
		}
		return Color.BLACK;
	}
	
	/**
	 * 
	 * @param url
	 * @param value
	 */
	public void setAgencyColorByUrl(String url, Color value) {
		agencyColorMap.put(url, value);
	}
	
	/**
	 * 
	 * @param metrics
	 * @param stats
	 */
	public void setColorGradient(List<GradientParameters> metrics, DescriptiveStatistics stats)
	{
		gradients.clear();
	    for (GradientParameters p : metrics ) {
	        gradients.add(p.createColorGradient(stats));
	    }
	    return;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public ColorGradient findColorGradiantByValue(double value)
	{
		 for ( ColorGradient cg : gradients) {
             if ( cg.contains(value)) { 
            	 return cg;
             }
		 }
		 return null;
	}

	/**
	 * @return the gradParms
	 */
	@JsonGetter("gradient_parameters")
	public List<GradientParameters> getGradParms() {
		return gradParms;
	}

	/**
	 * @param gradParms the gradParms to set
	 */
	@JsonSetter("gradient_parameters")
	public void setGradParms(List<GradientParameters> gradParms) {
		this.gradParms = gradParms;
	}

	/**
	 * @return the addRangeColor
	 */
	@JsonGetter("ADD_range_color")
	public Color getADDRangeColor() {
		return addRangeColor;
	}

	/**
	 * @param addRangeColor the addRangeColor to set
	 */
	@JsonSetter("ADD_range_color")
	public void setADDRangeColor(Color addRangeColor) {
		this.addRangeColor = addRangeColor;
	}

	/**
	 * @return the addAlphaValue
	 */
	@JsonGetter("ADD_alpha_value")
	public int getADDAlphaValue() {
		return addAlphaValue;
	}

	/**
	 * @param addAlphaValue the addAlphaValue to set
	 */
	@JsonSetter("ADD_alpha_value")
	public void setADDAlphaValue(int addAlphaValue) {
		this.addAlphaValue = addAlphaValue;
	}

	/**
	 * @return the clusterHiRange
	 */
	@JsonGetter("cluster_hi_range")
	public int getClusterHiRange() {
		return clusterHiRange;
	}

	/**
	 * @param clusterHiRange the clusterHiRange to set
	 */
	@JsonSetter("cluster_hi_range")
	public void setClusterHiRange(int clusterHiRange) {
		this.clusterHiRange = clusterHiRange;
	}

	/**
	 * @return the clusterLowRange
	 */
	@JsonGetter("cluster_low_range")
	public int getClusterLowRange() {
		return clusterLowRange;
	}

	/**
	 * @param clusterLowRange the clusterLowRange to set
	 */
	@JsonSetter("cluster_low_range")
	public void setClusterLowRange(int clusterLowRange) {
		this.clusterLowRange = clusterLowRange;
	}

	/**
	 * @return the clusterConfidence
	 */
	@JsonGetter("cluster_confidence")
	public double getClusterConfidence() {
		return clusterConfidence;
	}

	/**
	 * @param clusterConfidence the clusterConfidence to set
	 */
	@JsonSetter("cluster_confidence")
	public void setClusterConfidence(double clusterConfidence) {
		this.clusterConfidence = clusterConfidence;
	}

	/**
	 * @return the clusterDensity
	 */
	@JsonGetter("cluster_density")
	public double getClusterDensity() {
		return clusterDensity;
	}

	/**
	 * @param clusterDensity the clusterDensity to set
	 */
	@JsonSetter("cluster_density")
	public void setClusterDensity(double clusterDensity) {
		this.clusterDensity = clusterDensity;
	}

	/**
	 * @return the xbandWidth
	 */
	@JsonGetter("x_band_width")
	public IBandwidth getXbandWidth() {
		return xbandWidth;
	}

	/**
	 * @param xbandWidth the xbandWidth to set
	 */
	@JsonSetter("x_band_width")
	public void setXbandWidth(IBandwidth xbandWidth) {
		this.xbandWidth = xbandWidth;
	}

	/**
	 * @return the ybandWidth
	 */
	@JsonGetter("y_band_width")
	public IBandwidth getYbandWidth() {
		return ybandWidth;
	}

	/**
	 * @param ybandWidth the ybandWidth to set
	 */
	@JsonSetter("y_band_width")
	public void setYbandWidth(IBandwidth ybandWidth) {
		this.ybandWidth = ybandWidth;
	}

	/**
	 * @return the denstiyKernal
	 */
	@JsonGetter("denstiy_kernal")
	public IDensityKernel getDenstiyKernal() {
		return denstiyKernal;
	}

	/**
	 * @param denstiyKernal the denstiyKernal to set
	 */
	@JsonSetter("denstiy_kernal")
	public void setDenstiyKernal(IDensityKernel denstiyKernal) {
		this.denstiyKernal = denstiyKernal;
	}
	
	public void save()
	{
		StringBuilder fileName = new StringBuilder();
		
		fileName.append(System.getProperty("user.home"));
		fileName.append(File.separator);
		fileName.append(".");
		fileName.append(getClass().getSimpleName());
		fileName.append(".json");
		
		System.out.println("write: " + fileName.toString());
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(new File(fileName.toString()), this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ApplicationSettings load()
	{
		ApplicationSettings rtn = null;
		StringBuilder fileName = new StringBuilder();
		
		fileName.append(System.getProperty("user.home"));
		fileName.append(File.separator);
		fileName.append(".");
		fileName.append(ApplicationSettings.class.getSimpleName());
		fileName.append(".json");
		
		File fp = new File(fileName.toString());
		if ( ! fp.exists() ) { return rtn; }
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName.toString()));
			StringBuilder builder = new StringBuilder();
			while(in.ready()) {
				builder.append(in.readLine());
			}
			in.close();

			ObjectMapper objectMapper = new ObjectMapper();
			rtn = objectMapper.readValue(builder.toString(), ApplicationSettings.class);  
						
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rtn;
	}
	
}
