package me.transit.database.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import me.database.mongo.IDocument;
import me.transit.database.ServiceDate;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name="tran_route_geometry")
public class ServiceDateImpl extends TransitDateImpl implements ServiceDate{

	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias(ServiceDate.STARTDATE)
	@XStreamConverter(me.database.SingleValueCalendarConverter.class)
	private Calendar startDate = null;
	
	@XStreamAlias(ServiceDate.ENDDATE)
	@XStreamConverter(me.database.SingleValueCalendarConverter.class)
	private Calendar endDate = null;
	
	@XStreamAlias(ServiceDate.SERVICEDAYFLAG)
	private int serviceDayFlag = 0;
	
	@XStreamOmitField
	private ServiceDays service = ServiceDays.ALL_WEEK;
	
	/**
	 * @return the startDate
	 */
	@Column(name="START_DATE")
	@Type(type = "java.util.Calendar")
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
	@Column(name="END_DATE")
	@Type(type = "java.util.Calendar")
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
	@Column(name="SERVICE_DAYS")
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
	@Column(name="SERVICE_TYPE")
	@Enumerated(EnumType.STRING) 
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
	
	public void addServiceData(ServiceDate.WeekDay day)
	{
		this.serviceDayFlag = this.serviceDayFlag | day.getBit();
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
	public boolean equals(Object obj) {
		
		boolean rtn = false;
		if ( obj == null ) { return rtn; }
		if ( ServiceDate.class.isAssignableFrom(obj.getClass()) ) {
			ServiceDate rhs = ServiceDate.class.cast(obj);
			rtn = true;
			if ( ! this.getAgency().equals(rhs.getAgency()) ) {
				rtn = false;
			}
			if ( ! this.getEndDate().equals(rhs.getEndDate()) ) {
				rtn = false;
			}
			if ( ! this.getStartDate().equals(rhs.getStartDate()) ) {
				rtn = false;
			}
			if ( this.getService() != rhs.getService() ) {
				rtn = false;
			}
			if ( this.getServiceDayFlag() != rhs.getServiceDayFlag()) {
				rtn = false;
			}
			if ( ! this.getId().equals(rhs.getId()) ) {
				rtn = false;
			}
		}
		return rtn;
	}
	
    @Override
    public Map<String, Object> toDocument() {
            Map<String,Object> rtn = new HashMap<String,Object>();
            
            rtn.put(IDocument.CLASS, ServiceDateImpl.class.getName());
            rtn.put(IDocument.ID, this.getUUID());
            rtn.put(ServiceDate.STARTDATE, getStartDate().getTime());
            rtn.put(ServiceDate.ENDDATE, getEndDate().getTime());
            rtn.put(ServiceDate.SERVICE, this.getService().name());
            
            StringBuffer buffer = new StringBuffer();
            
            for (WeekDay day : ServiceDate.WeekDay.values()) {
                    if ( this.hasService(day) ) {
                            if ( buffer.length() > 0) { buffer.append(" "); }
                            buffer.append(day.name());
                    }
            }
            rtn.put(ServiceDate.SERVICEDAYFLAG, buffer);
            return rtn;
    }
    
    /**
     * 
     */
    @Override
    public void handleEnum(String key, Object value)
    {
            if ( key.equals(ServiceDate.SERVICE) ) {
                    this.setService( ServiceDate.ServiceDays.valueOf(value.toString()));
            } else if ( key.equals( ServiceDate.SERVICEDAYFLAG ) ) {
                    
                    String [] data = value.toString().split(" ");
                    for ( String day : data) {
                            this.addServiceData( ServiceDate.WeekDay.valueOf(day.trim() ));
                    }
            }
    }
	
	/**
	 * 
	 */
	public boolean valid() 
	{
		return true;
	}
	
}
