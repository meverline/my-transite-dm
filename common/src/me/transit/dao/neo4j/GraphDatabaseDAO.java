package me.transit.dao.neo4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;
import me.transit.database.impl.RouteStopDataImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;

public class GraphDatabaseDAO {
	
	public enum REL_TYPES implements RelationshipType
	{
	     HAS_STOP, AGENCY, LOCATION, HAS_A
	}
	
	public enum FIELD {
		db_id(true), 
		agency(true), 
		stop(true),
		trip(true) , 
		db_name(true), 
		className(false),
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

	private final static String DB_PATH = "/data/graph";
	
	private static GraphDatabaseDAO theOne = null;
	private GraphDatabaseService graphDb = null;
	private long foundCount = 0;
	private long numLocations = 0;
	
	public static Log log = LogFactory.getLog(GraphDatabaseDAO.class);


	/**
	 * 
	 */
	private GraphDatabaseDAO() 
	{
	    StringBuilder field_types = new StringBuilder();
	    for ( FIELD item : FIELD.values() ) {
	    	if ( item.isIndex() ) {
	    		if ( field_types.length() > 0 ) { field_types.append(","); }
	    		field_types.append(item.name());
	    	}
	    }
	    	
		StringBuilder rel_types = new StringBuilder();
		for ( REL_TYPES rel : REL_TYPES.values() ) {
		    if ( rel_types.length() > 0 ) { rel_types.append(","); }
		    rel_types.append(rel.name());
		}
	    
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( DB_PATH ).
											 setConfig( GraphDatabaseSettings.node_keys_indexable, field_types.toString() ).
											 setConfig( GraphDatabaseSettings.relationship_keys_indexable, rel_types.toString() ).
											 setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
											 setConfig( GraphDatabaseSettings.relationship_auto_indexing, "true" ).
											 newGraphDatabase();
		
		registerShutdownHook( graphDb );
	}
	
	/**
	 * 
	 * @param graphDb
	 */
	private void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized GraphDatabaseDAO instance() {
		if ( theOne == null ) {
			theOne = new GraphDatabaseDAO();
		}
		return theOne;
	}
	
	/**
	 * 
	 * @return
	 */
	private Transaction beginTransaction()
	{
		return graphDb.beginTx();
	}
		
	/**
	 * 
	 * @return
	 */
	public long getFoundCount()
	{
		return this.foundCount;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getNumLocations()
	{
		return this.numLocations;
	}
	
	/**
	 * 
	 * @param message
	 * @param ex
	 */
	private void logException(String message, Exception ex) {
		
		StringBuffer buffer = new StringBuffer(message + " " + ex.getLocalizedMessage());
		
		buffer.append("\n");
		StringWriter writer = new StringWriter();
		PrintWriter output = new PrintWriter(writer);
		ex.printStackTrace(output);
		
		buffer.append(writer.toString());
		log.error(buffer.toString());
		return;
	}
	
	/**
	 * 
	 * @param agency
	 * @return
	 */
	private Node addAgency(Agency agency) {
		Node node = graphDb.createNode();
        node.setProperty(FIELD.agency.name(), agency.getName());
        node.setProperty(FIELD.db_id.name(), agency.getId());
        return node;
	}
	
	/**
	 * 
	 * @param agency
	 * @return
	 */
	public Node addNode(Agency agency) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node node = index.get(FIELD.agency.name(), agency.getName()).getSingle();
		if ( node == null ) {
			Transaction tx = beginTransaction();
			try {
				node = this.addAgency(agency);
				tx.success();
			} catch ( Exception ex) {
			   tx.failure();
			   logException("Error adding: " + agency.getName(), ex );
			} finally {
				tx.finish();
			}
		}
        return node;
	}
	
	/**
	 * 
	 * @param stop
	 * @return
	 */
	public Node addNode(TransitStop stop) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node node = index.get(FIELD.stop.name(), FIELD.stop.makeKey(stop)).getSingle();
		if ( node == null ) {
			String key = FIELD.coordinate.makeKey(stop);
			Node coord = index.get(FIELD.coordinate.name(), key).getSingle();
					
			Transaction tx = beginTransaction();
			try {
				node = graphDb.createNode();
				node.setProperty( FIELD.stop.name(), FIELD.stop.makeKey(stop));
		        node.setProperty( FIELD.db_name.name(), stop.getName());
		        node.setProperty( FIELD.db_id.name(), stop.getId());
		        
		        if ( coord == null ) {
					coord = graphDb.createNode();
					coord.setProperty(FIELD.coordinate.name(), FIELD.coordinate.makeKey(stop));	
			        this.numLocations++;
				} else {
					this.foundCount++;
				}
		        
		        Relationship relationship = node.createRelationshipTo( coord, REL_TYPES.LOCATION );
		        relationship.setProperty(FIELD.className.name(), stop.getLocation().getClass().getSimpleName());
		        
		        createRelationShip(node, stop.getAgency());
		        tx.success();
			} catch ( Exception ex) {
				tx.failure();
				logException("Error adding route: " + FIELD.stop.makeKey(stop), ex );
			} finally {
				tx.finish();
			}
		}
        return node;
		
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 */
	public Node addNode(Route route) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node node = index.get(FIELD.route.name(), FIELD.route.makeKey(route)).getSingle();
		if ( node == null ) {
			Transaction tx = beginTransaction();
			try {
				node = graphDb.createNode();
				node.setProperty(FIELD.route.name(), FIELD.route.makeKey(route));
				node.setProperty(FIELD.db_name.name(), route.getShortName());
				node.setProperty(FIELD.db_id.name(), route.getId());
				createRelationShip(node, route.getAgency());
				tx.success();
			} catch (Exception ex) {
				tx.failure();
				logException("Error adding route: " + FIELD.route.makeKey(route), ex );
			} finally {
				tx.finish();
			}
		}
        return node;
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 */
	public Node addNode(Trip trip) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node node = index.get(FIELD.trip.name(), FIELD.trip.makeKey(trip)).getSingle();
		if ( node == null ) {
			Transaction tx = beginTransaction();
			try {
				node = graphDb.createNode();
				node.setProperty(FIELD.trip.name(), FIELD.trip.makeKey(trip));
				node.setProperty(FIELD.db_name.name(), trip.getHeadSign());
				if (trip.getShortName() != null) {
					node.setProperty(FIELD.db_id.name(), trip.getShortName());
				}
				createRelationShip(node, trip.getAgency());
				tx.success();
			} catch (Exception ex) {
				tx.failure();
				logException("Error adding trip: " + FIELD.trip.makeKey(trip), ex );
			} finally {
				tx.finish();
			}
		}
		return node;
	}
	
	/**
	 * 
	 * @param fromTrip
	 * @param toStop
	 * @return
	 */
	public boolean createRelationShip(Trip fromTrip, TransitStop toStop) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.stop.name(), FIELD.stop.makeKey(toStop)).getSingle();
		if ( to == null ) { 
			to = this.addNode(toStop);
		}
		
		Node from = index.get(FIELD.trip.name(), FIELD.trip.makeKey(fromTrip)).getSingle();
		if ( from == null ) { 
			from = this.addNode(fromTrip);
		}
		
		Transaction tx = beginTransaction();
		try {
			Relationship relationship = from.createRelationshipTo(to,REL_TYPES.HAS_A);
			relationship.setProperty(FIELD.className.name(), fromTrip.getClass().getSimpleName());
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip trip to stop: ", ex );
		} finally {
			tx.finish();
		}
		return true;
	}
	
	/**
	 * 
	 * @param fromTrip
	 * @param toStop
	 * @return
	 */
	public boolean createRelationShip(Route fromRoute, TransitStop toStop) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.stop.name(), FIELD.stop.makeKey(toStop)).getSingle();
		if ( to == null ) { 
			to = this.addNode(toStop);
		}
		
		Node from = index.get(FIELD.trip.name(), FIELD.trip.makeKey(fromRoute)).getSingle();
		if ( from == null ) { 
			from = this.addNode(fromRoute);
		}
		
		Transaction tx = beginTransaction();
		try {
		   Relationship relationship = from.createRelationshipTo( to, REL_TYPES.HAS_STOP );
		   relationship.setProperty( FIELD.className.name(), fromRoute.getClass().getSimpleName());
		   tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip route to stop: ", ex );
		} finally {
			tx.finish();
		}
		return true;
	}
	
	/**
	 * 
	 * @param fromRoute
	 * @param toTrip
	 * @return
	 */
	public boolean createRelationShip(Route fromRoute, Trip toTrip) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.trip.name(), FIELD.trip.makeKey(toTrip)).getSingle();
		if ( to == null ) { 
			to = this.addNode(toTrip);
		}
		
		Node from = index.get(FIELD.route.name(), FIELD.route.makeKey(fromRoute)).getSingle();
		if ( from == null ) { 
			from = this.addNode(fromRoute);
		}
		
		Transaction tx = beginTransaction();
		try {
		   Relationship relationship = from.createRelationshipTo( to, REL_TYPES.HAS_A );
		   relationship.setProperty( FIELD.className.name(), fromRoute.getClass().getSimpleName());
		   tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip route to trip: ", ex );
		} finally {
			tx.finish();
		}
		return true;
	}
	
	/**
	 * 
	 * @param from
	 * @param toAgency
	 * @return
	 */
	private boolean createRelationShip(Node from, Agency toAgency) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.agency.name(), toAgency.getName()).getSingle();
		if ( to == null ) { 
			to = this.addAgency(toAgency);
		}
		
	    from.createRelationshipTo( to, REL_TYPES.AGENCY );
		return true;
	}
		
	/**
	 * 
	 * @return
	 */
	public List<RouteStopData> findRoutes(TransitStop stop) {
				
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		FIELD type = FIELD.stop;
		String key = type.makeKey(stop);
		Node stopNode = index.get(type.name(), key).getSingle();
		
		TraversalDescription td = Traversal.description().breadthFirst().relationships(REL_TYPES.HAS_STOP, Direction.INCOMING).evaluator( Evaluators.excludeStartPosition());
		
		List<RouteStopData> rtn = new ArrayList<RouteStopData>();
		if ( stopNode != null ) {
			org.neo4j.graphdb.traversal.Traverser routes = td.traverse(stopNode);
			for ( Path path : routes) {
				RouteStopDataImpl item = new RouteStopDataImpl();
				
				Node node = path.endNode();
				item.setRouteShortName( node.getProperty(FIELD.db_name.name()).toString());
				item.setTripHeadSign( node.getProperty("tripHeadSign").toString());
				
				rtn.add(item);
			}
		}
		return rtn;
	}
	
	/**
	 * 
	 * @return
	 */
	public Node findNodeByField(FIELD fld, String id, String agency) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node rtn = index.get(FIELD.db_id.name(), id).getSingle();
		return rtn;
	}
	
	/**
	 * 
	 * @return
	 */
	public  List<Node> allRelationships(REL_TYPES type, Node node) {
		return null;
	}
	
}
