package me.openMap.models.query.sample;

import javax.swing.JCheckBox;

import me.transit.database.ServiceDate;

public class WeekdayCheckBox extends JCheckBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServiceDate.WeekDay day = null;

	/**
	 * 
	 * @param aDay
	 */
	public WeekdayCheckBox(ServiceDate.WeekDay aDay) {
		super(aDay.toString());
		day = aDay;
	}

	/**
	 * @return the day
	 */
	public ServiceDate.WeekDay getDay() {
		return day;
	}
			
}
