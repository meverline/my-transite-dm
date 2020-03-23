package me.transit.database;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tran_routetrip")
@DiscriminatorColumn(name = "routetrip_type")
@DiscriminatorValue("RouteTrip")
public class RouteTrip {
	
	@Id
	@Column(name = "ROUTE_TRIP_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;
	
	@Column(name = "TRIP_NDX", nullable = false)
	private int  ndx = 0;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIP_UUID", nullable = false, updatable = false)
	private Trip trip = null;
	
	/**
	 * 
	 */
	public RouteTrip() {
	}
	
	/**
	 * 
	 * @param routeId
	 * @param tripId
	 */
	public RouteTrip(int ndx, Trip trip) {
		this.setNdx(ndx);
		this.setTrip(trip);
	}

	/**
	 * @return the uuid
	 */
	public long getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the ndx
	 */
	public int getNdx() {
		return ndx;
	}

	/**
	 * @param ndx the ndx to set
	 */
	public void setNdx(int ndx) {
		this.ndx = ndx;
	}

	/**
	 * @return the trip
	 */
	public Trip getTrip() {
		return trip;
	}

	/**
	 * @param trip the trip to set
	 */
	public void setTrip(Trip trip) {
		this.trip = trip;
	}

}
