package me.transit.graph.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import me.transit.database.Agency;

@NodeEntity(label = "Agency")
public class AgencyNode extends TransiteNode {

	@GraphId
	private long id;

	@Property(name = "agency")
	private String name;
	@Property(name = "db_id")
	private long dbId;
	@Property(name = "className")
	private String className;

	public AgencyNode() {
	};

	public AgencyNode(Agency agency) {
		setName(agency.getName());
		setDbId(agency.getUUID());
		setClassName(agency.getClass().getName());
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

}
