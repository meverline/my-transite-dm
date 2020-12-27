package me.transit.database;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.json.CalendarJsonConvert;
import me.transit.json.JsonToCalendarConvert;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Objects;

@Entity(name = "CalendarDate")
@Table(name = "tran_calendar_date")
@DiscriminatorColumn(name = "calendar_date_type")
@DiscriminatorValue("CalendarDate")
@GTFSFileModel(filename="calendar_dates.txt")
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class CalendarDate extends TransitData {
	
	public enum ExceptionType { UNKNOWN, ADD_SERVICE, REMOVE_SERVICE }

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CALENDAR_DATE_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;

	@Column(name = "ID")
	private String id = null;

	@Column(name = "DATE")
	@Type(type = "java.util.Calendar")
	private Calendar date = Calendar.getInstance();

	@Column(name = "EXCCEPTOIN_TYPE")
	@Enumerated(EnumType.STRING)
	private ExceptionType exceptionType = ExceptionType.UNKNOWN;

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getUUID()
	 */
	@JsonGetter("uuid")
	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setUUID(long)
	 */
	@JsonSetter("uuid")
	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getId()
	 */
	@JsonGetter("service_id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setId(java.lang.String)
	 */
	@JsonSetter("service_id")
	@GTFSSetter(column="service_id")
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getDate()
	 */
	@JsonGetter("date")
	@JsonSerialize(converter = CalendarJsonConvert.class)
	public Calendar getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setDate(java.util.Calendar)
	 */
	@GTFSSetter(column="date")
	@JsonSetter("date")
	@JsonSerialize(converter = JsonToCalendarConvert.class)
	public void setDate(Calendar date) {
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getExceptionType()
	 */
	@JsonGetter("exception_type")
	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setExceptionType(me.transit.database.impl.CalendarDateImpl.ExceptionType)
	 */
	@GTFSSetter(column="exception_type")
	@JsonSetter("exception_type")
	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	@Override
	public String toString() {
		return "CalendarDate{" +
				"uuid=" + uuid +
				", agency=" + getAgency() +
				", id='" + id + '\'' +
				", version='" + getVersion() + '\'' +
				", exceptionType=" + exceptionType +
				", date=" + date.getTimeInMillis() +
				'}';
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#valid()
	 */

	public boolean valid() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CalendarDate that = (CalendarDate) o;
		return uuid == that.uuid &&
				Objects.equals(getAgency(), that.getAgency()) &&
				Objects.equals(id, that.id) &&
				Objects.equals(getVersion(), that.getVersion()) &&
				Objects.equals(date.getTimeInMillis(), that.date.getTimeInMillis()) &&
				exceptionType == that.exceptionType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, getAgency(), id, getVersion(), date, exceptionType);
	}
}
