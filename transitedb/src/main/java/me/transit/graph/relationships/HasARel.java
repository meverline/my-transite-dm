package me.transit.graph.relationships;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.StartNode;

public class HasARel {

    @Id 
    @GeneratedValue  
    private Long relationshipId;
    @Property            
    private String title;
    @StartNode           
    private Object startNode;
    @EndNode            
    private Object endNOde;
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
	 * @return the startNode
	 */
	public Object getStartNode() {
		return startNode;
	}
	/**
	 * @param startNode the startNode to set
	 */
	public void setStartNode(Object startNode) {
		this.startNode = startNode;
	}
	/**
	 * @return the endNOde
	 */
	public Object getEndNOde() {
		return endNOde;
	}
	/**
	 * @param endNOde the endNOde to set
	 */
	public void setEndNOde(Object endNOde) {
		this.endNOde = endNOde;
	}
    
    
}
