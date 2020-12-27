package me.transit.database;

import java.util.Calendar;
import java.util.Objects;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import me.transit.json.CalendarJsonConvert;
import me.transit.json.JsonToCalendarConvert;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.database.nsstore.AbstractDocument;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;

@Entity
@Table(name="tran_service_date")
@DiscriminatorColumn(name = "serviceDate_type")
@DiscriminatorValue("ServiceDate")
@GTFSFileModel(filename="calendar.txt")
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonFilter("agencyFilter")
public class ServiceDate extends TransitData {

	public static final String SERVICE = "service";

	public enum ServiceDays { ALL_WEEK, 
							  WEEKDAY_SERVICE,
							  WEEKDAY_SAT_SERVICE,
							  WEEKEND_SERVICE, 
							  SATURDAY_SERVICE,
							  SUNDAY_SERVICE }
							  
	public enum WeekDay {
		
		SUNDAY(0x1),
		MONDAY(0x2),
		TUESDAY(0x4),
		WENSDAY(0x8),
		THURSDAY(0x16),
		FRIDAY(0x32),
		SATURDAY(0x64);
		
		private final int bitFlag;
		
		WeekDay(int bit) {
			bitFlag = bit;
		}
		
		public int getBit() { return bitFlag; }
	}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SERVICE_DATE_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@GenericGenerator( name = "native", strategy = "native")	
	private long uuid = -1;

	@Column(name = "ID", nullable = false)
	private String id = null;

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
	 * @see me.transit.database.impl.ServiceDate#getStartDate()
	 */
	@JsonGetter("start_date")
	@JsonSerialize(converter = CalendarJsonConvert.class)
	public Calendar getStartDate() {
		return startDate;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setStartDate(java.util.Calendar)
	 */
	@GTFSSetter(column="start_date")
	@JsonSetter("start_date")
	@JsonSerialize(converter = JsonToCalendarConvert.class)
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getEndDate()
	 */
	@JsonGetter("end_date")
	@JsonSerialize(converter = CalendarJsonConvert.class)
	public Calendar getEndDate() {
		return endDate;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setEndDate(java.util.Calendar)
	 */
	@GTFSSetter(column="end_date")
	@JsonSetter("end_date")
	@JsonSerialize(converter = JsonToCalendarConvert.class)
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#getServiceDayFlag()
	 */
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
	@JsonGetter("service_days")
	public ServiceDays getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#setService(me.transit.database.impl.ServiceDateImpl.ServiceDays)
	 */
	@GTFSSetter(column="service_days")
	@JsonSetter("service_days")
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

	@JsonIgnore
	public boolean isSunday() {
		return hasService( ServiceDate.WeekDay.SUNDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isMonday()
	 */

	@JsonIgnore
	public boolean isMonday() {
		return hasService( ServiceDate.WeekDay.MONDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isTuesday()
	 */
	@JsonIgnore
	public boolean isTuesday() {
		return hasService( ServiceDate.WeekDay.TUESDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isWednesday()
	 */
	@JsonIgnore
	public boolean isWednesday() {
		return hasService( ServiceDate.WeekDay.WENSDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isThursday()
	 */
	@JsonIgnore
	public boolean isThursday() {
		return hasService( ServiceDate.WeekDay.THURSDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isFriday()
	 */
	@JsonIgnore
	public boolean isFriday() {
		return hasService( ServiceDate.WeekDay.FRIDAY);
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#isSaturday()
	 */
	@JsonIgnore
	public boolean isSaturday() {
		return hasService( ServiceDate.WeekDay.SATURDAY);
	}

    /* (non-Javadoc)
	 * @see me.transit.database.impl.ServiceDate#valid()
	 */
    @Override
	public boolean valid() 
	{
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServiceDate that = (ServiceDate) o;
		return uuid == that.uuid &&
				serviceDayFlag == that.serviceDayFlag &&
				Objects.equals(getAgency(), that.getAgency()) &&
				Objects.equals(id, that.id) &&
				Objects.equals(getVersion(), that.getVersion()) &&
				Objects.equals(startDate, that.startDate) &&
				Objects.equals(endDate, that.endDate) &&
				service == that.service;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, getAgency(), id, getVersion(), startDate, endDate, serviceDayFlag, service);
	}

	@Override
	public String toString() {
		return "ServiceDate{" +
				"uuid=" + uuid +
				", agency=" + getAgency() +
				", id='" + id + '\'' +
				", version='" + getVersion() + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", serviceDayFlag=" + serviceDayFlag +
				", service=" + service +
				'}';
	}
}
