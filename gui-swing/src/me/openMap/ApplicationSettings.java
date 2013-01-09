//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.datamining.Kernel.IDensityKernel;
import me.datamining.bandwidth.IBandwidth;
import me.utils.ColorGradient;
import me.utils.GradientParameters;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("ApplicationSettings")
public class ApplicationSettings {
	
	@XStreamOmitField
    private static ApplicationSettings theOne = null;

	
	private HashMap<String,Color> agencyColorMap = new HashMap<String,Color>();
    private List<GradientParameters> gradParms = new ArrayList<GradientParameters>();
 
    @XStreamAlias("addRangeColor")
    private Color addRangeColor = Color.blue;
    @XStreamAlias("addAlphaValue")
    private int addAlphaValue = 20;
    @XStreamAlias("clusterHiRange")
    private int clusterHiRange = Integer.MAX_VALUE;
    @XStreamAlias("clusterLowRange")
    private int clusterLowRange = 0;
    @XStreamAlias("clusterConfidence")
    private double clusterConfidence = 0.5;
    @XStreamAlias("clusterDensity")
    private double clusterDensity = 1;
    @XStreamAlias("xbandWidth")
    private IBandwidth xbandWidth = null;
    @XStreamAlias("ybandWidth")
    private IBandwidth ybandWidth = null;
    @XStreamAlias("denstiyKernal")
    private IDensityKernel denstiyKernal = null;
    
	@XStreamOmitField
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
	public List<GradientParameters> getGradParms() {
		return gradParms;
	}

	/**
	 * @param gradParms the gradParms to set
	 */
	public void setGradParms(List<GradientParameters> gradParms) {
		this.gradParms = gradParms;
	}

	/**
	 * @return the addRangeColor
	 */
	public Color getADDRangeColor() {
		return addRangeColor;
	}

	/**
	 * @param addRangeColor the addRangeColor to set
	 */
	public void setADDRangeColor(Color addRangeColor) {
		this.addRangeColor = addRangeColor;
	}

	/**
	 * @return the addAlphaValue
	 */
	public int getADDAlphaValue() {
		return addAlphaValue;
	}

	/**
	 * @param addAlphaValue the addAlphaValue to set
	 */
	public void setADDAlphaValue(int addAlphaValue) {
		this.addAlphaValue = addAlphaValue;
	}

	/**
	 * @return the clusterHiRange
	 */
	public int getClusterHiRange() {
		return clusterHiRange;
	}

	/**
	 * @param clusterHiRange the clusterHiRange to set
	 */
	public void setClusterHiRange(int clusterHiRange) {
		this.clusterHiRange = clusterHiRange;
	}

	/**
	 * @return the clusterLowRange
	 */
	public int getClusterLowRange() {
		return clusterLowRange;
	}

	/**
	 * @param clusterLowRange the clusterLowRange to set
	 */
	public void setClusterLowRange(int clusterLowRange) {
		this.clusterLowRange = clusterLowRange;
	}

	/**
	 * @return the clusterConfidence
	 */
	public double getClusterConfidence() {
		return clusterConfidence;
	}

	/**
	 * @param clusterConfidence the clusterConfidence to set
	 */
	public void setClusterConfidence(double clusterConfidence) {
		this.clusterConfidence = clusterConfidence;
	}

	/**
	 * @return the clusterDensity
	 */
	public double getClusterDensity() {
		return clusterDensity;
	}

	/**
	 * @param clusterDensity the clusterDensity to set
	 */
	public void setClusterDensity(double clusterDensity) {
		this.clusterDensity = clusterDensity;
	}

	/**
	 * @return the xbandWidth
	 */
	public IBandwidth getXbandWidth() {
		return xbandWidth;
	}

	/**
	 * @param xbandWidth the xbandWidth to set
	 */
	public void setXbandWidth(IBandwidth xbandWidth) {
		this.xbandWidth = xbandWidth;
	}

	/**
	 * @return the ybandWidth
	 */
	public IBandwidth getYbandWidth() {
		return ybandWidth;
	}

	/**
	 * @param ybandWidth the ybandWidth to set
	 */
	public void setYbandWidth(IBandwidth ybandWidth) {
		this.ybandWidth = ybandWidth;
	}

	/**
	 * @return the denstiyKernal
	 */
	public IDensityKernel getDenstiyKernal() {
		return denstiyKernal;
	}

	/**
	 * @param denstiyKernal the denstiyKernal to set
	 */
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
		fileName.append(".xml");
		
		System.out.println("write: " + fileName.toString());
		try {
			PrintStream ps = new PrintStream(new FileOutputStream(fileName.toString()));
			XStream stream = new XStream();
			
			stream.processAnnotations(getClass());
			stream.processAnnotations(GradientParameters.class);
			
			ps.println(stream.toXML(this));
			ps.close();
			
		} catch (FileNotFoundException e) {
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
		fileName.append(".xml");
		
		File fp = new File(fileName.toString());
		if ( ! fp.exists() ) { return rtn; }
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName.toString()));
			StringBuilder builder = new StringBuilder();
			while(in.ready()) {
				builder.append(in.readLine());
			}
			in.close();
			XStream stream = new XStream();
			
			stream.processAnnotations(ApplicationSettings.class);
			stream.processAnnotations(GradientParameters.class);
			
			Object obj = stream.fromXML(builder.toString());
			
			rtn = ApplicationSettings.class.cast(obj);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rtn;
	}
	
}
