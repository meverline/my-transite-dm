package me.transit.omd.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import me.transit.json.Base64StringToGeometry;
import me.transit.json.GeometryToBase64String;
import me.transit.omd.dao.json.GeometryFixer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "omd_locations")
@JsonDeserialize(converter = GeometryFixer.class)
public class Location implements Comparable<Location>, Serializable {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOCATION_UUID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator( name = "native", strategy = "native")
	private long uuid = -1;
	
	@Column(name = "ID", nullable = false)
	private int id = -1;
	
	@Column(name = "PID", nullable = false)
	private int pid = -1;
	
	@Column(name = "TITLE", nullable = false)
	private String title = null;
	
	@Column(name = "NAME", nullable = false)
	private String name = null;

	private transient double lat = -1.0;
	private transient double lon = -1.0;

	@Column(name = "LOCATION", columnDefinition = "Geometry")
	@Type(type="jts_geometry")
	private Point location = null;
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

	@JsonGetter("location")
	@JsonSerialize(converter = GeometryToBase64String.class)
	public Point getLocation() {  return location;  }

	@JsonSetter("location")
	@JsonDeserialize(converter = Base64StringToGeometry.class)
	public void setLocation(Point location) {
		this.location = location;
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
		return "Location{" +
				"uuid=" + uuid +
				", id=" + id +
				", pid=" + pid +
				", title='" + title + '\'' +
				", name='" + name + '\'' +
				", location=" + location +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return uuid == location.uuid &&
				id == location.id &&
				pid == location.pid &&
				Objects.equals(location.location, this.location) &&
				Objects.equals(title, location.title) &&
				Objects.equals(name, location.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, id, pid, title, name, location);
	}

}
