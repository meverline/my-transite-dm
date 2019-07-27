package me.database.neo4j;

import java.util.List;

import org.neo4j.graphdb.Node;

import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

public interface IGraphDatabaseDAO {
	
	public static final String BEAN_NAME = "graphdatabase";

	/**
	 * 
	 * @return
	 */
	long getFoundCount();

	/**
	 * 
	 * @return
	 */
	long getNumLocations();

	/**
	 * 
	 * @param agency
	 * @return
	 */
	Node addNode(Agency agency);

	/**
	 * 
	 * @param stop
	 * @return
	 */
	Node addNode(TransitStop stop);

	/**
	 * 
	 * @param route
	 * @return
	 */
	Node addNode(Route route);

	/**
	 * 
	 * @param route
	 * @return
	 */
	Node addNode(Trip trip);

	/**
	 * 
	 * @param fromTrip
	 * @param toStop
	 * @return
	 */
	boolean createRelationShip(Trip fromTrip, TransitStop toStop);

	/**
	 * 
	 * @param fromTrip
	 * @param toStop
	 * @return
	 */
	boolean createRelationShip(Route fromRoute, TransitStop toStop);

	/**
	 * 
	 * @param fromRoute
	 * @param toTrip
	 * @return
	 */
	boolean createRelationShip(Route fromRoute, Trip toTrip);

	/**
	 * 
	 * @return
	 */
	List<RouteStopData> findRoutes(TransitStop stop);

	/**
	 * 
	 * @return
	 */
	Node findNodeByField(FIELD fld, String key);

	/**
	 * 
	 * @return
	 */
	List<Node> allRelationships(REL_TYPES type, Node node);

}