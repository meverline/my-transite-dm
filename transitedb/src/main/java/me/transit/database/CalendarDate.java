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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity(name = "CalendarDate")
@Table(name = "tran_calendar_date")
@Inheritance
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

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getDate()
	 */

	public Calendar getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getUUID()
	 */

	public long getUUID() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setUUID(long)
	 */

	public void setUUID(long uuid) {
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getAgency()
	 */

	public Agency getAgency() {
		return agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setAgency(me.transit.database.impl.Agency)
	 */

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getId()
	 */

	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setId(java.lang.String)
	 */

	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getVersion()
	 */

	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setVersion(java.lang.String)
	 */

	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setDate(java.util.Calendar)
	 */

	public void setDate(Calendar date) {
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#getExceptionType()
	 */


	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	/* (non-Javadoc)
	 * @see me.transit.database.impl.CalendarDate#setExceptionType(me.transit.database.impl.CalendarDateImpl.ExceptionType)
	 */

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
