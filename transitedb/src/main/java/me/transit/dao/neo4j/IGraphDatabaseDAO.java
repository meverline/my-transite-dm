package me.transit.dao.neo4j;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

public interface IGraphDatabaseDAO {

	public enum REL_TYPES implements RelationshipType
	{
	     HAS_STOP, AGENCY, LOCATION, HAS_A, HEAD_SIGN
	}
	
	public enum FIELD {
		db_id(true), 
		agency(true), 
		stop(true),
		trip(true),
		trip_headSign(true) {
			public String makeKey(TransitData data) {
				Trip trip = Trip.class.cast(data);
				
				StringBuffer key = new StringBuffer();
				key.append(trip.getHeadSign());
				key.append("@");
				key.append(data.getAgency().getName());
				return key.toString();
			}
		},
		db_name(true), 
		className(false),
		direction(false),
		route(true) {
			public String makeKey(TransitData data) {
				Route route = Route.class.cast(data);
				
				StringBuffer key = new StringBuffer();
				key.append(route.getShortName());
				key.append("@");
				key.append(data.getAgency().getName());
				return key.toString();
			}
		}, 
		coordinate(true) {
			public String makeKey(TransitData data) {
				TransitStop stop = TransitStop.class.cast(data);
				
				StringBuffer key = new StringBuffer();
				key.append( stop.getLocation().getX() );
				key.append(",");
				key.append( stop.getLocation().getY() );
				return key.toString();
			}
		};
		
		private boolean index;
		FIELD(boolean toIndex) { this.index = toIndex; }
		
		public boolean isIndex() { return index; }
		public String makeKey(TransitData data) {
			StringBuffer key = new StringBuffer();
			key.append(data.getId());
			key.append("@");
			key.append(data.getAgency().getName());
			return key.toString();
		}
	}


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