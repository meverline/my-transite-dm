package me.transit.graph.relationships;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import me.transit.graph.nodes.RouteNode;
import me.transit.graph.nodes.StopNode;


@RelationshipEntity(type="HAS_STOP")
public class HasStop {
	
    @Id 
    @GeneratedValue  
    private Long relationshipId;
    @Property            
    private String title;
    @StartNode           
    private RouteNode route;
    @EndNode            
    private StopNode stop;
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
	public RouteNode getRoute() {
		return route;
	}
	/**
	 * @param route the route to set
	 */
	public void setRoute(RouteNode route) {
		this.route = route;
	}
	/**
	 * @return the stop
	 */
	public StopNode getStop() {
		return stop;
	}
	/**
	 * @param stop the stop to set
	 */
	public void setStop(StopNode stop) {
		this.stop = stop;
	}
    
}