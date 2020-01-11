package me.transit.graph.relationships;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.StartNode;

import me.transit.graph.nodes.AgencyNode;

public class AgencyRel {

    @Id 
    @GeneratedValue  
    private Long relationshipId;
    @Property            
    private String title;
    @StartNode           
    private Object route;
    @EndNode            
    private AgencyNode agency;
	/**
	 * @return the relationshipId
	 */
	public Long getRelationshipId() {
		return relationshipId;
	}
	/**
	 * @param relationshipId the relationshipId to set
	 */
	public void setRelationshipId(Long relationshipId) {
		this.relationshipId = relationshipId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the route
	 */
	public Object getRoute() {
		return route;
	}
	/**
	 * @param route the route to set
	 */
	public void setRoute(Object route) {
		this.route = route;
	}
	/**
	 * @return the agency
	 */
	public AgencyNode getAgency() {
		return agency;
	}
	/**
	 * @param agency the agency to set
	 */
	public void setAgency(AgencyNode agency) {
		this.agency = agency;
	}
    
}
