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
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.database.mongo.IDocument;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.json.AgencyToString;
import me.transit.json.StringToAgency;

@Entity
@Table(name="tran_service_date")
@Inheritance
@DiscriminatorColumn(name = "serviceDate_type")
@DiscriminatorValue("ServiceDateImpl")
@GTFSFileModel(filename="calendar.txt")
public class ServiceDate implements TransitData, IDocument {
	
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
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uuid = -1;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	@Column(name = "ID", nullable = false)
	private String id = null;

	@Column(name = "VERSION")
	private String version = "0.5";
	
	@Column(name="START_DATE")
	@Type(type = "java.util.Calendar")
	private Calendar startDate = null;
	
	@Column(name="END_DATE")
	@Type(type = "java.util.Calendar")
	private Calendar endDate = null;
	
	@Column(name="SERVICE_DAYS")
	private int serviceDayFlag = 0;
	
	@Column(name="SERVICE_TYPE")
	@Enumerated(EnumType.STRING) 
	private ServiceDays service = ServiceDays.ALL_WEEK;
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getUUID()
	 */
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setUUID(long)
	 */
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getAgency()
	 */
	@JsonGetter("agency_name")
	@JsonSerialize(converter = AgencyToString.class)
	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setAgency(me.transit.database.impl.Agency)
	 */
	@JsonSetter("agency_name")
	@JsonDeserialize(converter = StringToAgency.class)
	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getId()
	 */
	@JsonGetter("id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setId(java.lang.String)
	 */
	@GTFSSetter(column="service_id")
	@JsonSetter("id")
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getVersion()
	 */
	@JsonGetter("version")
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setVersion(java.lang.String)
	 */
	@GTFSSetter(column="version")
	@JsonSetter("version")
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getStartDate()
	 */
	@JsonGetter("start_date")
	public Calendar getStartDate() {
		return startDate;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setStartDate(java.util.Calendar)
	 */
	@GTFSSetter(column="start_date")
	@JsonSetter("start_date")
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getEndDate()
	 */
	@JsonGetter("end_date")
	public Calendar getEndDate() {
		return endDate;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setEndDate(java.util.Calendar)
	 */
	@GTFSSetter(column="end_date")
	@JsonSetter("end_date")
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getServiceDayFlag()
	 */
	@Column(name="SERVICE_DAYS")
	@JsonGetter("service_day_flag")
	public int getServiceDayFlag() {
		return serviceDayFlag;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setServiceDayFlag(int)
	 */
	@GTFSSetter(column="service_day_flag")
	@JsonSetter("service_day_flag")
	public void setServiceDayFlag(int serviceDayFlag) {
		this.serviceDayFlag = serviceDayFlag;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getService()
	 */
	@JsonGetter("sevice")
	public ServiceDays getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setService(me.transit.database.impl.ServiceDateImpl.ServiceDays)
	 */
	@GTFSSetter(column="sevice")
	@JsonSetter("sevice")
	public void setService(ServiceDays service) {
		this.service = service;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#hasService(me.transit.database.ServiceDate.WeekDay)
	 */

	public boolean hasService(ServiceDate.WeekDay day)
	{
		return ((this.serviceDayFlag & day.getBit()) == day.getBit());
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#addServiceData(me.transit.database.ServiceDate.WeekDay)
	 */

	public void addServiceData(ServiceDate.WeekDay day)
	{
		this.serviceDayFlag = this.serviceDayFlag | day.getBit();
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isSunday()
	 */

	public boolean isSunday() {
		return hasService( ServiceDate.WeekDay.SUNDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isMonday()
	 */

	public boolean isMonday() {
		return hasService( ServiceDate.WeekDay.MONDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isTuesday()
	 */

	public boolean isTuesday() {
		return hasService( ServiceDate.WeekDay.TUESDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isWednesday()
	 */

	public boolean isWednesday() {
		return hasService( ServiceDate.WeekDay.WENSDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isThursday()
	 */

	public boolean isThursday() {
		return hasService( ServiceDate.WeekDay.THURSDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isFriday()
	 */

	public boolean isFriday() {
		return hasService( ServiceDate.WeekDay.FRIDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isSaturday()
	 */

	public boolean isSaturday() {
		return hasService( ServiceDate.WeekDay.SATURDAY);
	}
		
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	public boolean equals(Object obj) {
		
		boolean rtn = false;
		if ( obj == null ) { return rtn; }
		if ( ServiceDate.class.isAssignableFrom(obj.getClass()) ) {
			ServiceDate rhs = ServiceDate.class.cast(obj);
			rtn = true;
			if (this.getAgency() != null && ! this.getAgency().equals(rhs.getAgency()) ) {
				rtn = false;
			}
			if ( this.getEndDate() != null && ! this.getEndDate().equals(rhs.getEndDate()) ) {
				rtn = false;
			}
			if ( this.getStartDate() != null && ! this.getStartDate().equals(rhs.getStartDate()) ) {
				rtn = false;
			}
			if ( this.getService() != rhs.getService() ) {
				rtn = false;
			}
			if ( this.getServiceDayFlag() != rhs.getServiceDayFlag()) {
				rtn = false;
			}
			if ( this.getId() != null && ! this.getId().equals(rhs.getId()) ) {
				rtn = false;
			}
		}
		return rtn;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#toDocument()
	 */
    @Override
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
    
    /* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#handleEnum(java.lang.String, java.lang.Object)
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
	
	/*
	 * (non-Javadoc)
	 * @see me.transit.database.TransitData#valid()
	 */
    /* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#valid()
	 */
    @Override
	public boolean valid() 
	{
		return true;
	}
	
}
