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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import me.transit.database.CalendarDate;

@Entity(name = "CalendarDate")
@Table(name = "tran_calendar_date")
@DiscriminatorColumn(name = "calendar_date_type")
@DiscriminatorValue("CalendarDateImpl")
@XStreamAlias("CalendarDate")
public class CalendarDate implements TransitData {

	public enum ExceptionType { ADD_SERVICE, REMOVE_SERVICE, UNKNOWN };

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

	@Column(name = "ID")
	@XStreamOmitField
	private String id = null;

	@Column(name = "VERSION")
	@XStreamAlias("version")
	private String version = "0.5";

	@Column(name = "DATE")
	@Type(type = "java.util.Calendar")
	private Calendar date = Calendar.getInstance();

	@Column(name = "EXCCEPTOIN_TYPE")
	@Enumerated(EnumType.STRING)
	private ExceptionType exceptionType = ExceptionType.UNKNOWN;

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @return the uuid
	 */
	public long getUUID() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the uuid to set
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
	 * @param agency
	 *            the agency to set
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
	 * @param id
	 *            the id to set
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
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the exceptionType
	 */

	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	/**
	 * @param exceptionType
	 *            the exceptionType to set
	 */
	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CalendarData: {" + super.toString() + "}");

		builder.append("date: " + this.getDate().getTime().toString());
		builder.append("\n");
		builder.append("exceptionType: " + this.getExceptionType());
		builder.append("\n");
		return builder.toString();
	}

	/**
	 * 
	 */
	public boolean valid() {
		return true;
	}

}
