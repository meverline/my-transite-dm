package me.database.neo4j;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;

import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

public class GraphDatabaseDAO implements IGraphDatabaseDAO {
	
	private final static String DB_PATH = "/data/graph";
	
	private static IGraphDatabaseDAO theOne = null;
	private GraphDatabaseService graphDb = null;
	private long foundCount = 0;
	private long numLocations = 0;
	private String dbPath = "/data/graph";
	
	public static Log log = LogFactory.getLog(IGraphDatabaseDAO.class);


	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
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
	    
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( new File(getDbPath())).
											 setConfig( GraphDatabaseSettings.node_keys_indexable, field_types.toString() ).
											 setConfig( GraphDatabaseSettings.relationship_keys_indexable, rel_types.toString() ).
											 setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
											 setConfig( GraphDatabaseSettings.relationship_auto_indexing, "true" ).
											 newGraphDatabase();
		
		registerShutdownHook( graphDb );
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDbPath() {
		return dbPath;
	}

	/**
	 * 
	 * @param dbPath
	 */
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
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
	
	protected void finalize()
	{
		if ( graphDb != null ) {
			graphDb.shutdown();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IGraphDatabaseDAO instance() {
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
		
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#getFoundCount()
	 */
	@Override
	public long getFoundCount()
	{
		return this.foundCount;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#getNumLocations()
	 */
	@Override
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
	 * @param obj
	 * @return
	 */
	private String getInterface(Object obj)
	{
		String className = obj.getClass().getSimpleName();
		int ndx = className.indexOf("Impl");
		if ( ndx != -1 ) {
			return className.substring(0, ndx);
		}
		return className;
	}
	
	/**
	 * 
	 * @param agency
	 * @return
	 */
	private Node addAgency(Agency agency) {
		Map<String, String> properites = agency.getProperties();
		Node node = graphDb.createNode();
		for ( Entry<String, String> entry : properites.entrySet()) {
			node.setProperty(entry.getKey(), entry.getValue());	
		}
        return node;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.Agency)
	 */
	@Override
	@SuppressWarnings("deprecation")
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
				tx.close();
			}
		}
        return node;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.TransitStop)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public Node addNode(TransitStop stop) {
		
		Map<String, String> properites = stop.getProperties();
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node node = index.get(FIELD.stop.name(), stop.makeKey()).getSingle();
		if ( node == null ) {
			String key = stop.makeCoordinateKey();
			Node coord = index.get(FIELD.coordinate.name(), key).getSingle();
					
			Transaction tx = beginTransaction();
			try {
				node = graphDb.createNode();
				for ( Entry<String, String> entry : properites.entrySet()) {
					if (entry.getKey() != FIELD.coordinate.name()) {
						node.setProperty(entry.getKey(), entry.getValue());
					}
				}
				
		        if ( coord == null ) {
					coord = graphDb.createNode();
					coord.setProperty(FIELD.coordinate.name(), stop.makeCoordinateKey());	
			        this.numLocations++;
				} else {
					this.foundCount++;
				}
		        
		        Relationship relationship = node.createRelationshipTo( coord, REL_TYPES.LOCATION );
		        relationship.setProperty(FIELD.className.name(), this.getInterface(stop.getLocation()));
		        
		        createRelationShip(node, stop.getAgency());
		        tx.success();
			} catch ( Exception ex) {
				tx.failure();
				logException("Error adding route: " + stop.makeKey(), ex );
			} finally {
				tx.close();
			}
		}
        return node;
		
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.Route)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public Node addNode(Route route) {
		Map<String, String> properites = route.getProperties();
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node node = index.get(FIELD.route.name(), route.makeKey()).getSingle();
		if ( node == null ) {
			Transaction tx = beginTransaction();
			try {
				node = graphDb.createNode();
				for ( Entry<String, String> entry : properites.entrySet()) {
					node.setProperty(entry.getKey(), entry.getValue());
				}
				
				createRelationShip(node, route.getAgency());
				tx.success();
			} catch (Exception ex) {
				tx.failure();
				logException("Error adding route: " + route.makeKey(), ex );
			} finally {
				tx.close();
			}
		}
        return node;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.Trip)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public Node addNode(Trip trip) {
		Map<String, String> properites = trip.getProperties();
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node node = index.get(FIELD.trip.name(), trip.makeKey()).getSingle();
		Node hs = index.get(FIELD.trip_headSign.name(), trip.makeHeadSignKey() ).getSingle();

		if ( node == null ) {
			Transaction tx = beginTransaction();
			try {
				node = graphDb.createNode();
				for ( Entry<String, String> entry : properites.entrySet()) {
					node.setProperty(entry.getKey(), entry.getValue());
				}
				
				createRelationShip(node, trip.getAgency());
				
				if ( hs == null ) {
					hs = graphDb.createNode();
					hs.setProperty(FIELD.trip_headSign.name(), trip.makeHeadSignKey());
					createRelationShip(hs, trip.getAgency());
				}
				
				tx.success();
			} catch (Exception ex) {
				tx.failure();
				logException("Error adding trip: " + trip.makeKey(), ex );
			} finally {
				tx.close();
			}
		}
		return node;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#createRelationShip(me.transit.database.Trip, me.transit.database.TransitStop)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean createRelationShip(Trip fromTrip, TransitStop toStop) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.stop.name(), toStop.makeKey()).getSingle();
		if ( to == null ) { 
			to = this.addNode(toStop);
		}
		
		Node from = index.get(FIELD.trip.name(), fromTrip.makeKey()).getSingle();
		if ( from == null ) { 
			from = this.addNode(fromTrip);
		}
		
		if ( to == null || from == null ) {
			log.warn("Warng adding relationsip trip to stop: nodes null: " + from  + " to "+ to);
			return false;
		}

		Transaction tx = beginTransaction();
		try {
			Relationship relationship = from.createRelationshipTo(to, REL_TYPES.HAS_A);
			relationship.setProperty(FIELD.className.name(), this.getInterface(fromTrip));
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip trip to stop: ", ex );
		} finally {
			tx.close();
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#createRelationShip(me.transit.database.Route, me.transit.database.TransitStop)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean createRelationShip(Route fromRoute, TransitStop toStop) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.stop.name(), toStop.makeKey()).getSingle();
		if ( to == null ) { 
			to = this.addNode(toStop);
		}
		
		Node from = index.get(FIELD.route.name(), fromRoute.makeKey()).getSingle();
		if ( from == null ) { 
			from = this.addNode(fromRoute);
		}
		
		if ( to == null || from == null ) {
			log.warn("Warng adding relationsip route to stop: nodes null: " + from  + " to "+ to);
			return false;
		}
		
		Transaction tx = beginTransaction();
		try {
		   Relationship relationship = from.createRelationshipTo( to, REL_TYPES.HAS_STOP );
		   relationship.setProperty( FIELD.className.name(), this.getInterface(fromRoute));
		   tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip route to stop: ", ex );
		} finally {
			tx.close();
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#createRelationShip(me.transit.database.Route, me.transit.database.Trip)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean createRelationShip(Route fromRoute, Trip toTrip) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.trip.name(), toTrip.makeKey()).getSingle();
		if ( to == null ) { 
			 to = this.addNode(toTrip);
		} 
		
		Node from = index.get(FIELD.route.name(), fromRoute.makeKey()).getSingle();
		if ( from == null ) { 
			from = this.addNode(fromRoute);
		}
		
		Node tohs = index.get(FIELD.trip_headSign.name(), toTrip.makeHeadSignKey() ).getSingle();

		if ( to == null || from == null ) {
			log.warn("Warng adding relationsip route to trip: nodes null: " + from  + " to "+ to);
			return false;
		}
				
		Transaction tx = beginTransaction();
		try {
		   Relationship relationship = from.createRelationshipTo( to, REL_TYPES.HAS_A );
		   relationship.setProperty( FIELD.className.name(), this.getInterface(fromRoute));

		   relationship = from.createRelationshipTo( tohs, REL_TYPES.HEAD_SIGN );
		   relationship.setProperty( FIELD.className.name(), this.getInterface(fromRoute));
   
		   tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip route to trip: ", ex );
		} finally {
			tx.close();
		}
		return true;
	}
	
	/**
	 * 
	 * @param from
	 * @param toAgency
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean createRelationShip(Node from, Agency toAgency) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node to = index.get(FIELD.agency.name(), toAgency.getName()).getSingle();
		if ( to == null ) { 
			to = this.addAgency(toAgency);
		}
		
	    from.createRelationshipTo( to, REL_TYPES.AGENCY );
		return true;
	}
		
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#findRoutes(me.transit.database.TransitStop)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public List<RouteStopData> findRoutes(TransitStop stop) {
				
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node stopNode = index.get(FIELD.stop.name(), stop.makeKey()).getSingle();
				
		List<RouteStopData> rtn = new ArrayList<RouteStopData>();
		if ( stopNode != null ) {
			
			TraversalDescription td = graphDb.traversalDescription().depthFirst().
												 relationships(REL_TYPES.HAS_A, Direction.INCOMING).
												 evaluator( Evaluators.excludeStartPosition());
			
			org.neo4j.graphdb.traversal.Traverser routes = td.traverse(stopNode);
			String currentRoute = null;
			for ( Path path : routes) {
				
				Node node = path.endNode();		
								
				String className = node.getProperty(FIELD.className.name()).toString();
				String name = node.getProperty(FIELD.db_name.name()).toString();
								
				if ( className.equals(Route.class.getSimpleName()) ) {
					
					if ( currentRoute == null ) {
						currentRoute = name;
					} else if ( ! currentRoute.equals(name) ) {
						currentRoute = name;
					}
					
				} else if ( className.equals(Trip.class.getSimpleName()) ) {
					RouteStopData item = new RouteStopData();

					item.setRouteShortName( currentRoute );
					item.setTripHeadSign( name );
					rtn.add(item);
				}
				
			}
		}
		return rtn;
	}
	
	protected String nodeToString(Node node)
	{
		StringBuilder item = new StringBuilder();
		item.append("{");
		for ( String prop : node.getPropertyKeys() )
		{
			item.append("[ " + prop + " " + node.getProperty(prop) + "]" );
		}
		item.append("}");
		return item.toString();
	}
	
	protected void showRelationships(Node rtn, String start) 
	{
		for ( Relationship rel : rtn.getRelationships()) {
			
			Node endNode = rel.getEndNode();
			String cn = "";
			if ( rel.hasProperty(FIELD.className.name())) {
			   cn = rel.getProperty(FIELD.className.name()).toString();
			}
			
			System.out.println(start + " " + rel.getType().name() + " "  + 
										   cn + " : " + nodeToString(endNode) );
		}
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#findNodeByField(me.transit.dao.neo4j.GraphDatabaseDAO.FIELD, java.lang.String)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public Node findNodeByField(FIELD fld, String key) {
		
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		Node rtn = index.get(fld.name(), key).getSingle();	
		return rtn;
	}
	
	/* (non-Javadoc)
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#allRelationships(me.transit.dao.neo4j.GraphDatabaseDAO.REL_TYPES, org.neo4j.graphdb.Node)
	 */
	@Override
	public  List<Node> allRelationships(REL_TYPES type, Node node) {
		return null;
	}
	
}
