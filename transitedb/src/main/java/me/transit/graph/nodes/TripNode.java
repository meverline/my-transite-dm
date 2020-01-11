package me.transit.graph.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;

import me.transit.database.Trip;

public class TripNode extends TransiteNode {

	@GraphId
	private long id;

	@Property(name = "trip")
	private String name;
	@Property(name = "db_name")
	private String dbName;
	@Property(name = "db_id")
	private long dbId;
	@Property(name = "className")
	private String className;
	@Property(name = "direction")
	private String direction;
	@Property(name = "trip_headSign")
	private String headSign;

	public TripNode() {
	}

	public TripNode(Trip trip) {
		this.setClassName(trip.getClass().getName());
		this.setDbId(trip.getUUID());
		this.setDbName(trip.getHeadSign());
		this.setDirection(trip.getDirectionId().toString());
		this.setHeadSign(this.makeHeadSignKey(trip));
		this.setName(this.makeKey(trip));
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the dbId
	 */
	public long getDbId() {
		return dbId;
	}

	/**
	 * @param dbId the dbId to set
	 */
	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return the headSign
	 */
	public String getHeadSign() {
		return headSign;
	}

	/**
	 * @param headSign the headSign to set
	 */
	public void setHeadSign(String headSign) {
		this.headSign = headSign;
	}

	public String makeHeadSignKey(Trip trip) {
		StringBuffer key = new StringBuffer();
		key.append(trip.getHeadSign());
		key.append("@");
		key.append(trip.getAgency().getName());
		return key.toString();
	}

}
