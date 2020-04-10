package me.utils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ColorGradient {
	private ArrayList<MapColor> gradiant_ = null;
	private ArrayList<Double> steps_ = null;
	private double high_ = 0.0;
	private double low_ = 0.0;
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
		high_ = high;
		low_ = low;
		gradiant_ = createGradiant(start, end, theNumSteps);
		setUpSetSize(low, high, theNumSteps);
		this.setAlhpaValue(alphaValue);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAlhpaValue() {
		return alhpaValue;
	}

	/**
	 * 
	 * @param alhpaValue
	 */
	private void setAlhpaValue(int alhpaValue) {
		this.alhpaValue = alhpaValue;
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
	 * @return
	 */
	public double getLow() {
		return low_;
	}

	/**
	 * 
	 * @return
	 */
	public double getHigh() {
		return high_;
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
	
	public static class MapColor {

		private Color primary_ = Color.BLACK;
		private String primName_ = "black";
		private int max_ = 0;

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
			primary_ = getColor(getPrimaryName(), getPrimaryColor());
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

			primary_ = name;
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

		/**
		 * 
		 * @return
		 */
		public String getPrimaryName() {
			return primName_;
		}

		/**
		 * 
		 * @param name_
		 */
		public void setPrimaryName(String name_) {
			this.primName_ = name_;
		}

		/**
		 * 
		 * @return
		 */
		public int getCount() {
			return max_;
		}

		/**
		 * 
		 * @param max_
		 */
		public void setCount(int max_) {
			this.max_ = max_;
		}

		/**
		 * 
		 * @return
		 */
		public Color getPrimaryColor() {
			return primary_;
		}

	}

}
