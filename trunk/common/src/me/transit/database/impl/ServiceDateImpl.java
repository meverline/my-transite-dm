package me.transit.database.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.transit.dao.mongo.IDocument;
import me.transit.database.ServiceDate;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class ServiceDateImpl extends TransitDateImpl implements ServiceDate{

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	@XStreamAlias("startDate")
	@XStreamConverter(me.database.SingleValueCalendarConverter.class)
	private Calendar startDate = null;
	@XStreamAlias("endDate")
	@XStreamConverter(me.database.SingleValueCalendarConverter.class)
	private Calendar endDate = null;
	@XStreamAlias("serviceDayFlag")
	private int serviceDayFlag = 0;
	@XStreamOmitField
	private ServiceDays service = ServiceDays.ALL_WEEK;
	
	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @return the serviceDayFlag
	 */
	public int getServiceDayFlag() {
		return serviceDayFlag;
	}

	/**
	 * @param serviceDayFlag the serviceDayFlag to set
	 */
	public void setServiceDayFlag(int serviceDayFlag) {
		this.serviceDayFlag = serviceDayFlag;
	}

	/**
	 * @return the service
	 */
	public ServiceDays getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ServiceDays service) {
		this.service = service;
	}
	
	public boolean hasService(ServiceDate.WeekDay day)
	{
		return ((this.serviceDayFlag & day.getBit()) == day.getBit());
	}

	/**
	 * @return the sunday
	 */
	public boolean isSunday() {
		return hasService( ServiceDate.WeekDay.SUNDAY);
	}

	/**
	 * @return the monday
	 */
	public boolean isMonday() {
		return hasService( ServiceDate.WeekDay.MONDAY);
	}

	/**
	 * @return the tuesday
	 */
	public boolean isTuesday() {
		return hasService( ServiceDate.WeekDay.TUESDAY);
	}

	/**
	 * @return the wensday
	 */
	public boolean isWednesday() {
		return hasService( ServiceDate.WeekDay.WENSDAY);
	}

	/**
	 * @return the thursday
	 */
	public boolean isThursday() {
		return hasService( ServiceDate.WeekDay.THURSDAY);
	}

	/**
	 * @return the friday
	 */
	public boolean isFriday() {
		return hasService( ServiceDate.WeekDay.FRIDAY);
	}

	/**
	 * @return the saturday
	 */
	public boolean isSaturday() {
		return hasService( ServiceDate.WeekDay.SATURDAY);
	}

	@Override
	public Map<String, Object> toDocument() {
		Map<String,Object> rtn = new HashMap<String,Object>();

		SimpleDateFormat sdf = new SimpleDateFormat();
		rtn.put(IDocument.CLASS, ServiceDateImpl.class.getName());
		rtn.put(IDocument.ID, this.getUUID());
		rtn.put("startDate", sdf.format(new Date(getStartDate().get(Calendar.MILLISECOND))));
		rtn.put("endDate", sdf.format(new Date(getEndDate().get(Calendar.MILLISECOND))));
		
		List<String> str = new ArrayList<String>();
		
		for (WeekDay day : ServiceDate.WeekDay.values()) {
			if ( this.hasService(day) ) {
				str.add(day.name());
			}
		}
		rtn.put("serviceDayFlag", str);
		return rtn;
	}
	
	@Override
	public String getCollection() {
		return ServiceDate.COLLECTION;
	}

	@Override
	public void fromDocument(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}
}
