package me.transit.dao.neo4j;

import java.util.ArrayList;
import java.util.List;

import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.impl.RouteStopDataImpl;

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

import com.vividsolutions.jts.geom.Point;

public class GraphDatabaseDAO {
	
	public enum REL_TYPES implements RelationshipType
	{
	     HAS_STOP, HAS_ROUTE, AGENCY, OWNS_STOP, RUNS_ROUTE, LOCATION
	}
	
	public enum FIELD {
		db_id(true), 
		agency(true), 
		stop(true) {
			public String makeKey(String value, String agency) {
				StringBuffer key = new StringBuffer();
				key.append(value);
				key.append("@");
				key.append(agency);
				return key.toString();
			}
		},
		route(true) {
			public String makeKey(String value, String agency) {
				StringBuffer key = new StringBuffer();
				key.append(value);
				key.append("@");
				key.append(agency);
				return key.toString();
			}
		}, 
		db_name(true), 
		coordinate(true) {
			public String makeKey(String value, String agency) {
				StringBuffer key = new StringBuffer();
				key.append( value);
				key.append(",");
				key.append( agency);
				return key.toString();
			}
		};
		
		private boolean index;
		FIELD(boolean toIndex) { this.index = toIndex; }
		
		public boolean isIndex() { return index; }
		public String makeKey(String value, String agency) {
			return value;
		}
	}

	private final static String DB_PATH = "/data/graph";
	
	private static GraphDatabaseDAO theOne = null;
	private GraphDatabaseService graphDb = null;
	private long foundCount = 0;
	private long numLocations = 0;

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
	 * @param agency
	 * @return
	 */
	public Node addNode(Agency agency) {
		Transaction tx = graphDb.beginTx();
		Node node = graphDb.createNode();
        node.setProperty(FIELD.agency.name(), agency.getName());
        node.setProperty(FIELD.db_id.name(), agency.getId());
        tx.success();
        tx.finish();
        return node;
	}
	
	protected Node addLocationNode(Point stop, Node stopNode) {
				
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		FIELD type = FIELD.coordinate;
		String key = type.makeKey(Double.toString(stop.getX()), Double.toString(stop.getY()));
		Node coord = index.get(type.name(), key).getSingle();
	
		if ( coord == null ) {
			Transaction tx = graphDb.beginTx();
			coord = graphDb.createNode();
			coord.setProperty(FIELD.coordinate.name(), key);
			tx.success();
	        tx.finish();
	        this.numLocations++;
		} else {
			this.foundCount++;
		}
		this.createRelationShip(stopNode, coord, REL_TYPES.LOCATION);
        return coord;
	}
	
	public long getFoundCount()
	{
		return this.foundCount;
	}
	
	public long getNumLocations()
	{
		return this.numLocations;
	}
	
	/**
	 * 
	 * @param stop
	 * @return
	 */
	public Node addNode(TransitStop stop) {
				
		Transaction tx = graphDb.beginTx();
		Node node = graphDb.createNode();
		node.setProperty( FIELD.stop.name(), FIELD.stop.makeKey(stop.getName(), stop.getAgency().getName()));
        node.setProperty( FIELD.db_name.name(), stop.getName());
        node.setProperty( FIELD.db_id.name(), stop.getId());
        tx.success();
        tx.finish();
        this.addLocationNode(stop.getLocation(), node);
        return node;
		
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 */
	public Node addNode(Route route) {
		
		Transaction tx = graphDb.beginTx();
		Node node = graphDb.createNode();
        node.setProperty( FIELD.route.name(), FIELD.route.makeKey(route.getShortName(),  route.getAgency().getName()) );
        node.setProperty( FIELD.db_name.name(), route.getShortName());
        node.setProperty( FIELD.db_id.name(), route.getId());
        tx.success();
        tx.finish();
        return node;
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 */
	public Relationship createRelationShip(Node from, Node to, REL_TYPES relation) {
		Transaction tx = graphDb.beginTx();
		Relationship relationship = from.createRelationshipTo( to, relation );
		tx.success();
		tx.finish();
		return relationship;
	}
	
	/**
	 * 
	 */
	public void createIndex() {
		 
		return;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RouteStopData> findRoutes(TransitStop stop) {
				
		ReadableIndex<Node> index = graphDb.index().getNodeAutoIndexer().getAutoIndex();
		
		FIELD type = FIELD.stop;
		String key = type.makeKey(stop.getName(), stop.getAgency().getName());
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
