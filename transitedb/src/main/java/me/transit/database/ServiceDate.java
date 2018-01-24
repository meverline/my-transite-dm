package me.transit.database;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import me.database.mongo.IDocument;
import me.transit.database.ServiceDate;

@Entity
@Table(name="tran_service_date")
@DiscriminatorColumn(name = "serviceDate_type")
@DiscriminatorValue("ServiceDateImpl")
@XStreamAlias("ServiceDate")
public class ServiceDate implements TransitData {

	public static final String STARTDATE = "startDate";
	public static final String ENDDATE = "endDate";
	public static final String SERVICE = "service";
	public static final String SERVICEDAYFLAG = "serviceDayFlag";
	
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
	
	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XStreamAlias("id")
	private long uuid = -1;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	@Column(name = "ID", nullable = false)
	@XStreamOmitField
	private String id = null;

	@Column(name = "VERSION")
	@XStreamAlias("version")
	private String version = "0.5";
	
	@Column(name="START_DATE")
	@Type(type = "java.util.Calendar")
	@XStreamAlias(ServiceDate.STARTDATE)
	@XStreamConverter(me.database.SingleValueCalendarConverter.class)
	private Calendar startDate = null;
	
	@Column(name="END_DATE")
	@Type(type = "java.util.Calendar")
	@XStreamAlias(ServiceDate.ENDDATE)
	@XStreamConverter(me.database.SingleValueCalendarConverter.class)
	private Calendar endDate = null;
	
	@Column(name="SERVICE_DAYS")
	@XStreamAlias(ServiceDate.SERVICEDAYFLAG)
	private int serviceDayFlag = 0;
	
	@Column(name="SERVICE_TYPE")
	@Enumerated(EnumType.STRING) 
	@XStreamOmitField
	private ServiceDays service = ServiceDays.ALL_WEEK;
	
	/**
	 * @return the uuid
	 */
	public long getUUID() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the agency
	 */
	public Agency getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

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
	public ServiceDays getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ServiceDays service) {
		this.service = service;
	}
	
	/**
	 * 
	 * @param day
	 * @return
	 */
	public boolean hasService(ServiceDate.WeekDay day)
	{
		return ((this.serviceDayFlag & day.getBit()) == day.getBit());
	}
	
	/**
	 * 
	 * @param day
	 */
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
		
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
	
	/**
	 * 
	 * @return
	 */
    public Map<String, Object> toDocument() {
            Map<String,Object> rtn = new HashMap<String,Object>();
            
            rtn.put(IDocument.CLASS, ServiceDate.class.getName());
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
     * @param key
     * @param value
     */
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
	
	/*
	 * (non-Javadoc)
	 * @see me.transit.database.TransitData#valid()
	 */
    @Override
	public boolean valid() 
	{
		return true;
	}
	
}
