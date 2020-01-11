package me.transit.graph.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;

import me.transit.database.Route;

public class RouteNode extends TransiteNode {

	@GraphId
	private long id;

	@Property(name = "route")
	private String name;
	@Property(name = "db_name")
	private String db_name;
	@Property(name = "db_id")
	private long dbId;
	@Property(name = "className")
	private String className;

	public RouteNode() {
	}

	public RouteNode(Route route) {
		this.setClassName(route.getClass().getName());
		this.setDbId(route.getUUID());
		this.setDbname(route.getShortName());
		this.setName(this.makeKey(route));
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
	 * @return the db_name
	 */
	public String getDbname() {
		return db_name;
	}

	/**
	 * @param db_name the db_name to set
	 */
	public void setDbname(String db_name) {
		this.db_name = db_name;
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
	 * 
	 * @param route
	 * @return
	 */
	public String makeKey(Route route) {
		StringBuffer key = new StringBuffer();
		key.append(route.getShortName());
		key.append("@");
		key.append(route.getAgency().getName());
		return key.toString();
	}

}
