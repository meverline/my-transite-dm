package me.utils;

import lombok.Getter;
import lombok.Setter;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;

@Setter
@Getter
public class ColorGradient {
	private ArrayList<MapColor> gradiant_ = null;
	private ArrayList<Double> steps_ = null;
	private double high = 0.0;
	private double low = 0.0;
	private int alhpaValue = 85;

	/**
	 * 
	 * @param start
	 * @param end
	 * @param theNumSteps
	 * @param low
	 * @param high
	 * @param alphaValue
	 */
	public ColorGradient(Color start, Color end, int theNumSteps, double low,
			double high, int alphaValue) {
		this.high = high;
		this.low = low;
		gradiant_ = createGradiant(start, end, theNumSteps);
		setUpSetSize(low, high, theNumSteps);
		this.setAlhpaValue(alphaValue);
	}

	/**
	 * 
	 * @param low
	 * @param high
	 * @param theNumSteps
	 */
	private void setUpSetSize(double low, double high, int theNumSteps) {
		double rangeStep = Math.abs(low - high) / theNumSteps - 2;

		double count = low;
		steps_ = new ArrayList<Double>();
		steps_.add(low);
		for (int ndx = 1; ndx < theNumSteps; ndx++) {
			steps_.add(count + rangeStep);
		}
		steps_.add(high);
	}

	/**
	 * 
	 * @param pBegin
	 * @param pEnd
	 * @param pStep
	 * @param pMax
	 * @return
	 */
	private int interpolate(int pBegin, int pEnd, int pStep, int pMax) {
		double step = pStep;
		double max = pMax;
		double value = pBegin;
		if (pBegin < pEnd) {
			value = ((pEnd - pBegin) * (step / max)) + pBegin;
		} else {
			value = ((pBegin - pEnd) * (1 - (step / max))) + pEnd;
		}
		return (int) Math.floor(value + 0.5f);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param theNumSteps
	 * @return
	 */
	private ArrayList<MapColor> createGradiant(Color start, Color end,
			int theNumSteps) {
		ArrayList<MapColor> rtn = new ArrayList<MapColor>();
		for (int i = 0; i <= theNumSteps; i++) {
			int theR = interpolate(start.getRed(), end.getRed(), i, theNumSteps);
			int theG = interpolate(start.getGreen(), end.getGreen(), i,
					theNumSteps);
			int theB = interpolate(start.getBlue(), end.getBlue(), i,
					theNumSteps);

			Color clr = new Color(theR, theG, theB);
			rtn.add(new MapColor(clr.toString(), clr, i));
		}
		return rtn;
	}

	/**
	 * 
	 * @param max
	 * @param startColor
	 * @param endColor
	 */
	public void createColorGradiant(int max, Color startColor, Color endColor) {
		int steps = (int) Math.floor((max / 10.0) + 0.5);
		gradiant_ = createGradiant(startColor, endColor, steps);
		return;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public boolean contains(double value) {
		if (value >= getLow() && value <= getHigh()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	public Color findHeatMapColor(double number) {
		for (int ndx = 0; ndx < steps_.size(); ndx++) {
			if (steps_.get(ndx) > number) {
				if (ndx - 1 < 0) {
					return gradiant_.get(ndx).getPrimaryColor();
				}
				return gradiant_.get(ndx - 1).getPrimaryColor();
			}
		}
		return gradiant_.get(gradiant_.size() - 1).getPrimaryColor();
	}

	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

	@Setter
	@Getter
	public static class MapColor {

		private Color primaryColor = Color.BLACK;
		private String primaryName = "black";
		private int count = 0;

		/**
		 * 
		 */
		public MapColor() {
		}

		/**
		 * 
		 * @param name
		 * @param count
		 */
		public MapColor(String name, int count) {
			setPrimaryName(name);
			setCount(count);
			primaryColor = getColor(getPrimaryName(), getPrimaryColor());
		}

		/**
		 * 
		 * @param colorName
		 * @param name
		 * @param min
		 */
		public MapColor(String colorName, Color name, int min) {
			setPrimaryName(colorName);
			setCount(min);
			primaryColor = name;
		}

		/**
		 * 
		 * @param name
		 * @param old
		 * @return
		 */
		private Color getColor(String name, Color old) {
			Color rtn = old;

			if (getPrimaryName().length() > 0) {
				Field field;
				try {
					field = Color.class.getField(name);
					rtn = (Color) field.get(null);
				} catch (Exception e) {
					rtn = Color.BLACK;
				}
			}
			return rtn;
		}

	}

}
