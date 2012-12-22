package me.transit.database;

import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Maps to Goggles - General Tranist Feed Specification
 * calendar.txt.
 * 
 * @author markeverline
 *
 */
@XStreamAlias("ServiceDate")
public interface ServiceDate extends TransitData {
	
	public enum ServiceDays { ALL_WEEK, 
							  WEEKDAY_SERVICE,
							  WEEKDAY_SAT_SERVICE,
							  WEEKEND_SERVICE, 
							  SATURDAY_SERVICE,
							  SUNDAY_SERVICE };
							  
	public enum WeekDay {
		
		SUNDAY(0x1),
		MONDAY(0x2),
		TUESDAY(0x4),
		WENSDAY(0x8),
		THURSDAY(0x16),
		FRIDAY(0x32),
		SATURDAY(0x64);
		
		private int bitFlag = 0x0;
		
		WeekDay(int bit) {
			bitFlag = bit;
		}
		
		public int getBit() { return bitFlag; }
	}
		
	/**
	 * @return the startDate
	 */
	public Calendar getStartDate();

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate);

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate();

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate);
	
	/**
	 * @return the serviceDayFlag
	 */
	public int getServiceDayFlag();

	/**
	 * @param serviceDayFlag the serviceDayFlag to set
	 */
	public void setServiceDayFlag(int serviceDayFlag);

	/**
	 * @return the service
	 */
	public ServiceDays getService();

	/**
	 * @param service the service to set
	 */
	public void setService(ServiceDays service);
	
	public boolean hasService(ServiceDate.WeekDay day);

	/**
	 * @return the sunday
	 */
	public boolean isSunday();
	/**
	 * @return the monday
	 */
	public boolean isMonday();
	/**
	 * @return the tuesday
	 */
	public boolean isTuesday();
	/**
	 * @return the wensday
	 */
	public boolean isWednesday();
	
	/**
	 * @return the thursday
	 */
	public boolean isThursday() ;

	/**
	 * @return the friday
	 */
	public boolean isFriday();
	/**
	 * @return the saturday
	 */
	public boolean isSaturday();
	
}
