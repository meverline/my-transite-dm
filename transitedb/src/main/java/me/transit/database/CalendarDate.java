package me.transit.database;

import java.util.Calendar;

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

import me.transit.annotation.GTFSFileModel;
import me.transit.annotation.GTFSSetter;
import me.transit.json.AgencyToString;
import me.transit.json.StringToAgency;

@Entity(name = "CalendarDate")
@Table(name = "tran_calendar_date")
@Inheritance
@DiscriminatorColumn(name = "calendar_date_type")
@DiscriminatorValue("CalendarDateImpl")
@GTFSFileModel(filename="calendar_dates.txt")
public class CalendarDate implements TransitData {
	
	public enum ExceptionType { ADD_SERVICE, REMOVE_SERVICE, UNKNOWN };

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uuid = -1;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "AGENCY_UUID", nullable = false, updatable = false)
	private Agency agency = null;

	@Column(name = "ID")
	private String id = null;

	@Column(name = "VERSION")
	private String version = "0.5";

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
	 * @see me.transit.database.impl.CalendarDate#getAgency()
	 */
	@JsonGetter("agency_name")
	@JsonSerialize(converter = AgencyToString.class)
	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setAgency(me.transit.database.impl.Agency)
	 */
	@JsonSetter("agency_name")
	@JsonDeserialize(converter = StringToAgency.class)
	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getId()
	 */
	@GTFSSetter(column="service_id")
	@JsonGetter("service_id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setId(java.lang.String)
	 */
	@JsonSetter("service_id")
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getVersion()
	 */
	@JsonGetter("version")
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setVersion(java.lang.String)
	 */
	@GTFSSetter(column="version")
	@JsonSetter("version")
	public void setVersion(String version) {
		this.version = version;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getDate()
	 */
	@JsonGetter("date")
	public Calendar getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setDate(java.util.Calendar)
	 */
	@GTFSSetter(column="date")
	@JsonSetter("date")
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


	public String toString() {
		StringBuilder builder = new StringBuilder("CalendarData: {" + super.toString() + "}");

		builder.append("date: " + this.getDate().getTime().toString());
		builder.append("\n");
		builder.append("exceptionType: " + this.getExceptionType());
		builder.append("\n");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#valid()
	 */

	public boolean valid() {
		return true;
	}

}
