package me.transit.dao.neo4j;

import java.util.List;

import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.TransitStop;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GraphDatabaseDAO {
	
	public enum REL_TYPES implements RelationshipType
	{
	     HAS_STOP, HAS_ROUTE, AGENCY, OWNS_STOP, RUNS_ROUTE
	}
	
	public enum FIELD {
		db_id, agency, stop, route, db_name
	}

	private final static String DB_PATH = "/data/graph";
	
	private static GraphDatabaseDAO theOne = null;
	private GraphDatabaseService graphDb = null;

	/**
	 * 
	 */
	private GraphDatabaseDAO() 
	{
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
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
        return node;
	}
	
	/**
	 * 
	 * @param stop
	 * @return
	 */
	public Node addNode(TransitStop stop) {
		
		StringBuffer key = new StringBuffer();
		key.append(stop.getName());
		key.append("@");
		key.append(stop.getAgency().getName());
		
		Transaction tx = graphDb.beginTx();
		Node node = graphDb.createNode();
        node.setProperty( FIELD.stop.name(), key.toString());
        node.setProperty( FIELD.db_name.name(), stop.getName());
        node.setProperty( FIELD.agency.name(), stop.getAgency().getName());
        node.setProperty( FIELD.db_id.name(), stop.getId());
        tx.success();
        return node;
		
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 */
	public Node addNode(Route route) {
		StringBuffer key = new StringBuffer();
		key.append(route.getShortName());
		key.append("@");
		key.append(route.getAgency().getName());
		
		Transaction tx = graphDb.beginTx();
		Node node = graphDb.createNode();
        node.setProperty( FIELD.route.name(), key.toString());
        node.setProperty( FIELD.db_name.name(), route.getShortName());
        node.setProperty( FIELD.agency.name(), route.getAgency().getName());
        node.setProperty( FIELD.db_id.name(), route.getId());
        tx.success();
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
	public Node findNodeByField(FIELD fld, String id, String agency) {
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public  List<Node> allRelationships(REL_TYPES type, Node node) {
		return null;
	}
	
}
