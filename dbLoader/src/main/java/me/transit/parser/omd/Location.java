package me.transit.parser.omd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@Table(name = "omd_locations")
public class Location implements Comparable<Location>  {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOCATION_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uuid = -1;
	
	@Column(name = "ID", nullable = false)
	private int id;
	
	@Column(name = "PID", nullable = false)
	private int pid;
	
	@Column(name = "TITLE", nullable = false)
	private String title;
	
	@Column(name = "NAME", nullable = false)
	private String name;
	
	@Column(name = "LAT", nullable = false)
	private double lat;
	
	@Column(name = "LON", nullable = false)
	private double lon;
	/**
	 * @return the id
	 */
	@JsonGetter("id")
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	@JsonSetter("id")
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the pid
	 */
	@JsonGetter("pid")
	public int getPid() {
		return pid;
	}
	/**
	 * @param pid the pid to set
	 */
	@JsonSetter("pid")
	public void setPid(int pid) {
		this.pid = pid;
	}
	/**
	 * @return the title
	 */
	@JsonGetter("t")
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	@JsonSetter("t")
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the name
	 */
	@JsonGetter("n")
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	@JsonSetter("n")
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the lat
	 */
	@JsonGetter("lat")
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	@JsonSetter("lat")
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	@JsonGetter("lon")
	public double getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	@JsonSetter("lng")
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Location l) {
		return l.getName().compareTo(getName()) + 
				   l.getTitle().compareTo(getTitle());
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.getTitle();
	}
	
}
