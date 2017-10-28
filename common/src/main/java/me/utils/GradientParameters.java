package me.utils;

import java.awt.Color;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("GradientParameters")
public class GradientParameters {
	
	@XStreamOmitField
	private Color startColor = Color.WHITE;
	@XStreamOmitField
	private Color endColor = Color.BLACK;
	@XStreamAlias("startColorString")
	private String startColorString = "White";
	@XStreamAlias("endColorString")
	private String endColorString = "Black";
	@XStreamAlias("startPercential")
	private double startPercential = 0.1;
	@XStreamAlias("range")
	private double range = 99.9;
	@XStreamAlias("steps")
	private int steps = 10;
	@XStreamAlias("alphaValue")
	private int alphaValue = 85;
	
	
	public GradientParameters() {
	}
	
	public GradientParameters(String start, String end, double startPercential,
			  double range, int steps, int alphaValue) {
		this.setStartColorString(start);
		this.setEndColorString(end);
		this.setStartPercential(startPercential);
		this.setRange(range);
		this.setSteps_(steps);
		this.setAlphaValue(alphaValue);
	}


	/**
	 * 
	 * @param start
	 * @param end
	 * @param startPercential
	 * @param range
	 * @param steps
	 * @param alphaValue
	 */
	public GradientParameters(Color start, Color end, double startPercential,
							  double range, int steps, int alphaValue) {
		this.setStartColor(start);
		this.setEndColor(end);
		this.setStartPercential(startPercential);
		this.setRange(range);
		this.setSteps_(steps);
		this.setAlphaValue(alphaValue);
	}
	
	/**
	 * @return the startColorString
	 */
	public String getStartColorString() {
		return startColorString;
	}

	/**
	 * @param startColorString the startColorString to set
	 */
	public void setStartColorString(String startColorString) {
		this.startColorString = startColorString;
		if ( startColorString.startsWith("#")) {
			this.setStartColor(Color.decode(startColorString));
		} else {
			this.setStartColor(Color.getColor(startColorString));
		}
	}

	/**
	 * @return the endColorString
	 */
	public String getEndColorString() {
		return endColorString;
	}

	/**
	 * @param endColorString the endColorString to set
	 */
	public void setEndColorString(String endColorString) {
		this.endColorString = endColorString;
		if ( endColorString.startsWith("#")) {
			this.setEndColor(Color.decode(endColorString));
		} else {
			this.setEndColor(Color.getColor(endColorString));
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getAlphaValue() {
		return this.alphaValue;
	}

	/**
	 * 
	 * @param alphaValue
	 */
	public void setAlphaValue(int alphaValue) {
		this.alphaValue = alphaValue;
	}

	/**
	 * 
	 * @return
	 */
	public int getSteps_() {
		return this.steps;
	}

    /**
     * 
     * @param steps
     */
	public void setSteps_(int steps) {
		this.steps = steps;
	}

	/**
	 * 
	 * @return
	 */
	public Color getStartColor() {
		return this.startColor;
	}

	/**
	 * 
	 * @param startColor
	 */
	public void setStartColor(Color startColor) {
		this.startColor = startColor;
	}

	/**
	 * 
	 * @return
	 */
	public Color getEndColor() {
		return this.endColor;
	}

	/**
	 * 
	 * @param endColor
	 */
	public void setEndColor(Color endColor) {
		this.endColor = endColor;
	}

	/**
	 * 
	 * @return
	 */
	public double getStartPercential() {
		return this.startPercential;
	}

	/**
	 * 
	 * @param startPercential
	 */
	public void setStartPercential(double startPercential) {
		this.startPercential = startPercential;
	}

	/**
	 * 
	 * @return
	 */
	public double getRange() {
		return this.range;
	}
    
	/**
	 * 
	 * @param range
	 */
	public void setRange(double range) {
		this.range = range;
	}

	/**
	 * 
	 * @param stats
	 * @return
	 */
	public ColorGradient createColorGradient(DescriptiveStatistics stats) {
		
		double endRange = this.getStartPercential() + this.getRange();
		return new ColorGradient(this.getStartColor(), this.getEndColor(),
								 this.getSteps_(),
								 stats.getPercentile(this.getStartPercential()),
								 stats.getPercentile(endRange), this.getAlphaValue());

	}

}
