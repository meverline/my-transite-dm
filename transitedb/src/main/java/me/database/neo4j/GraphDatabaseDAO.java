package me.database.neo4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
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
	private String dbPath = GraphDatabaseDAO.DB_PATH;

	public static Log log = LogFactory.getLog(IGraphDatabaseDAO.class);

	/**
	 * 
	 */
	private GraphDatabaseDAO() {
		StringBuilder field_types = new StringBuilder();
		for (FIELD item : FIELD.values()) {
			if (item.isIndex()) {
				if (field_types.length() > 0) {
					field_types.append(",");
				}
				field_types.append(item.name());
			}
		}

		StringBuilder rel_types = new StringBuilder();
		for (REL_TYPES rel : REL_TYPES.values()) {
			if (rel_types.length() > 0) {
				rel_types.append(",");
			}
			rel_types.append(rel.name());
		}

		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(new File(getDbPath()))
				.newGraphDatabase();

		registerShutdownHook(graphDb);
		indexDatabase();
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
	private void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
	
	/**
	 * 
	 * @param label
	 * @param field
	 * @return
	 */
	private IndexDefinition createIndex(Label label,  String field)
	{
		IndexDefinition indexDefinition;
        try ( Transaction tx = graphDb.beginTx() )
        {
            indexDefinition = graphDb.schema().indexFor( label ).on( field ).create();
            tx.success();
        }
        return indexDefinition;
	}
	
	/**
	 * 
	 */
    private void indexDatabase() {
    	Map<String, String> indexMap = new HashMap<>();
    	
    	indexMap.put(Agency.class.getSimpleName(), FIELD.agency.name());
    	indexMap.put(TransitStop.class.getSimpleName(), FIELD.stop.name());
    	indexMap.put(Trip.class.getSimpleName(), FIELD.trip.name());
    	indexMap.put(Route.class.getSimpleName(), FIELD.route.name());
    	indexMap.put("Point", FIELD.coordinate.name());
    	indexMap.put("HeadSign", FIELD.trip_headSign.name());
    	
    	List<IndexDefinition> indexs = new ArrayList<>();
    	for ( Map.Entry<String, String> item : indexMap.entrySet()) {
    		try {
    			indexs.add(createIndex(Label.label(item.getKey()), item.getValue()));
    		} catch (Exception ex){  
    			log.warn("indexing exception: " + ex.getLocalizedMessage());
    		}
    	}
    	
    }
	
	/**
	 * 
	 */
	protected void finalize() {
		if (graphDb != null) {
			graphDb.shutdown();
		}
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized IGraphDatabaseDAO instance() {
		if (theOne == null) {
			theOne = new GraphDatabaseDAO();
		}
		return theOne;
	}

	/**
	 * 
	 * @return
	 */
	private Transaction beginTransaction() {
		return graphDb.beginTx();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#getFoundCount()
	 */
	@Override
	public long getFoundCount() {
		return this.foundCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#getNumLocations()
	 */
	@Override
	public long getNumLocations() {
		return this.numLocations;
	}

	/**
	 * 
	 * @param message
	 * @param ex
	 */
	private void logException(String message, Exception ex) {

		StringBuffer buffer = new StringBuffer(message + " " + ex.getLocalizedMessage());
		log.error(buffer.toString(), ex);
		return;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	private String getInterface(Object obj) {
		String className = obj.getClass().getSimpleName();
		int ndx = className.indexOf("Impl");
		if (ndx != -1) {
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
		Node node = graphDb.createNode(Label.label(agency.getClass().getSimpleName()));
		for (Entry<String, String> entry : properites.entrySet()) {
			node.setProperty(entry.getKey(), entry.getValue());
		}
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.Agency)
	 */
	@Override
	public Node addNode(Agency agency) {

		Node node = null;
		Transaction tx = beginTransaction();
		try {
			node = graphDb.findNode(Label.label(Agency.class.getName()), FIELD.agency.name(), agency.getName());
			if (node == null) {
				node = this.addAgency(agency);
			}
			tx.success();

		} catch (Exception ex) {
			tx.failure();
			logException("Error adding: " + agency.getName(), ex);
		} finally {
			tx.close();
		}
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.
	 * TransitStop)
	 */
	@Override
	public Node addNode(TransitStop stop) {
		Node node = null;
		Map<String, String> properites = stop.getProperties();

		Transaction tx = beginTransaction();
		try {
			
			node = graphDb.findNode(Label.label(stop.getClass().getSimpleName()),
													FIELD.stop.name(), stop.makeKey());
			String key = stop.makeCoordinateKey();
			Node coord = graphDb.findNode(Label.label("Point"),
												FIELD.coordinate.name(), key);

			if (node == null) {
				node = graphDb.createNode(Label.label(stop.getClass().getName()));
				for (Entry<String, String> entry : properites.entrySet()) {
					if (entry.getKey() != FIELD.coordinate.name()) {
						node.setProperty(entry.getKey(), entry.getValue());
					}
				}

				if (coord == null) {
					coord = graphDb.createNode(Label.label("Point"));
					coord.setProperty(FIELD.coordinate.name(), stop.makeCoordinateKey());
					this.numLocations++;
				} else {
					this.foundCount++;
				}

				Relationship relationship = node.createRelationshipTo(coord, REL_TYPES.LOCATION);
				relationship.setProperty(FIELD.className.name(), this.getInterface(stop.getLocation()));

				createRelationShip(node, stop.getAgency());
			}
			tx.success();

		} catch (Exception ex) {
			tx.failure();
			logException("Error adding route: " + stop.makeKey(), ex);
		} finally {
			tx.close();
		}
		return node;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.Route)
	 */
	@Override
	public Node addNode(Route route) {
		Map<String, String> properites = route.getProperties();
		Node node = null;
		Transaction tx = beginTransaction();
		try {
			node = graphDb.findNode(Label.label(Route.class.getSimpleName()),
										FIELD.route.name(), route.makeKey());
			if (node == null) {

				node = graphDb.createNode(Label.label(route.getClass().getSimpleName()));
				for (Entry<String, String> entry : properites.entrySet()) {
					node.setProperty(entry.getKey(), entry.getValue());
				}

				createRelationShip(node, route.getAgency());
			}
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding route: " + route.makeKey(), ex);
		} finally {
			tx.close();
		}

		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#addNode(me.transit.database.Trip)
	 */
	@Override
	public Node addNode(Trip trip) {
		Map<String, String> properites = trip.getProperties();
		Node node = null;
		Transaction tx = beginTransaction();
		try {

			node = graphDb.findNode(Label.label(trip.getClass().getSimpleName()), 
											FIELD.trip.name(), trip.makeKey());
			Node hs = graphDb.findNode(Label.label("HeadSign"), 
											FIELD.trip_headSign.name(), trip.makeHeadSignKey());

			if (node == null) {
				node = graphDb.createNode(Label.label(trip.getClass().getSimpleName()));
				for (Entry<String, String> entry : properites.entrySet()) {
					node.setProperty(entry.getKey(), entry.getValue());
				}

				createRelationShip(node, trip.getAgency());

				if (hs == null) {
					hs = graphDb.createNode(Label.label("HeadSign"));
					hs.setProperty(FIELD.trip_headSign.name(), trip.makeHeadSignKey());
					createRelationShip(hs, trip.getAgency());
				}
			}
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding trip: " + trip.makeKey(), ex);
		} finally {
			tx.close();
		}

		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.transit.dao.neo4j.IGraphDatabaseDAO#createRelationShip(me.transit.database
	 * .Trip, me.transit.database.TransitStop)
	 */
	@Override
	public boolean createRelationShip(Trip fromTrip, TransitStop toStop) {

		Transaction tx = beginTransaction();
		try {

			Node to = graphDb.findNode(Label.label(toStop.getClass().getSimpleName()),
												FIELD.stop.name(), toStop.makeKey());
			if (to == null) {
				to = this.addNode(toStop);
			}

			Node from = graphDb.findNode(Label.label(fromTrip.getClass().getSimpleName()),
											FIELD.trip.name(), fromTrip.makeKey());
			if (from == null) {
				from = this.addNode(fromTrip);
			}

			if (to == null || from == null) {
				log.warn("Warng adding relationsip trip to stop: nodes null: " + from + " to " + to);
				tx.success();
				return false;
			}

			Relationship relationship = from.createRelationshipTo(to, REL_TYPES.HAS_A);
			relationship.setProperty(FIELD.className.name(), this.getInterface(fromTrip));
			tx.success();

		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip trip to stop: ", ex);
		} finally {
			tx.close();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.transit.dao.neo4j.IGraphDatabaseDAO#createRelationShip(me.transit.database
	 * .Route, me.transit.database.TransitStop)
	 */
	@Override
	public boolean createRelationShip(Route fromRoute, TransitStop toStop) {

		Transaction tx = beginTransaction();
		try {
			Node to = graphDb.findNode(Label.label(toStop.getClass().getSimpleName()),
											FIELD.stop.name(), toStop.makeKey());
			if (to == null) {
				to = this.addNode(toStop);
			}

			Node from = graphDb.findNode(Label.label(fromRoute.getClass().getSimpleName()),
											FIELD.route.name(), fromRoute.makeKey());
			if (from == null) {
				from = this.addNode(fromRoute);
			}

			if (to == null || from == null) {
				log.warn("Warng adding relationsip route to stop: nodes null: " + from + " to " + to);
				tx.success();
				return false;
			}

			Relationship relationship = from.createRelationshipTo(to, REL_TYPES.HAS_STOP);
			relationship.setProperty(FIELD.className.name(), this.getInterface(fromRoute));
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip route to stop: ", ex);
		} finally {
			tx.close();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.transit.dao.neo4j.IGraphDatabaseDAO#createRelationShip(me.transit.database
	 * .Route, me.transit.database.Trip)
	 */
	@Override
	public boolean createRelationShip(Route fromRoute, Trip toTrip) {

		Transaction tx = beginTransaction();
		try {
			Node to = graphDb.findNode(Label.label(toTrip.getClass().getSimpleName()), 
											FIELD.trip.name(), toTrip.makeKey());
			if (to == null) {
				to = this.addNode(toTrip);
			}

			Node from = graphDb.findNode(Label.label(fromRoute.getClass().getSimpleName()),
											FIELD.route.name(), fromRoute.makeKey());
			if (from == null) {
				from = this.addNode(fromRoute);
			}

			Node tohs = graphDb.findNode(Label.label("HeadSign"), FIELD.trip_headSign.name(), 
								toTrip.makeHeadSignKey());

			if (to == null || from == null) {
				log.warn("Warng adding relationsip route to trip: nodes null: " + from + " to " + to);
				tx.success();
				return false;
			}

			Relationship relationship = from.createRelationshipTo(to, REL_TYPES.HAS_A);
			relationship.setProperty(FIELD.className.name(), this.getInterface(fromRoute));

			relationship = from.createRelationshipTo(tohs, REL_TYPES.HEAD_SIGN);
			relationship.setProperty(FIELD.className.name(), this.getInterface(fromRoute));

			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip route to trip: ", ex);
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
	private boolean createRelationShip(Node from, Agency toAgency) {

		Transaction tx = beginTransaction();
		try {
			Node to = graphDb.findNode(Label.label(toAgency.getClass().getName()), 
											FIELD.agency.name(), toAgency.getName());
			if (to == null) {
				to = this.addAgency(toAgency);
			}

			from.createRelationshipTo(to, REL_TYPES.AGENCY);
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip node to agency: ", ex);
		} finally {
			tx.close();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#findRoutes(me.transit.database.
	 * TransitStop)
	 */
	@Override
	public List<RouteStopData> findRoutes(TransitStop stop) {

		List<RouteStopData> rtn = new ArrayList<RouteStopData>();
		Transaction tx = beginTransaction();
		try {
			Node stopNode = graphDb.findNode(Label.label(stop.getClass().getSimpleName()), 
												FIELD.stop.name(), stop.makeKey());
			if (stopNode != null) {

				TraversalDescription td = graphDb.traversalDescription().depthFirst()
						.relationships(REL_TYPES.HAS_A, Direction.INCOMING)
						.evaluator(Evaluators.excludeStartPosition());

				org.neo4j.graphdb.traversal.Traverser routes = td.traverse(stopNode);
				String currentRoute = null;
				for (Path path : routes) {

					Node node = path.endNode();

					String className = node.getProperty(FIELD.className.name()).toString();
					String name = node.getProperty(FIELD.db_name.name()).toString();

					if (className.equals(Route.class.getSimpleName())) {

						if (currentRoute == null) {
							currentRoute = name;
						} else if (!currentRoute.equals(name)) {
							currentRoute = name;
						}

					} else if (className.equals(Trip.class.getSimpleName())) {
						RouteStopData item = new RouteStopData();

						item.setRouteShortName(currentRoute);
						item.setTripHeadSign(name);
						rtn.add(item);
					}

				}
			}
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip node to agency: ", ex);
		} finally {
			tx.close();
		}
		return rtn;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	protected String nodeToString(Node node) {
		StringBuilder item = new StringBuilder();
		item.append("{");
		for (String prop : node.getPropertyKeys()) {
			item.append("[ " + prop + " " + node.getProperty(prop) + "]");
		}
		item.append("}");
		return item.toString();
	}

	/**
	 * 
	 * @param rtn
	 * @param start
	 */
	protected void showRelationships(Node rtn, String start) {
		for (Relationship rel : rtn.getRelationships()) {

			Node endNode = rel.getEndNode();
			String cn = "";
			if (rel.hasProperty(FIELD.className.name())) {
				cn = rel.getProperty(FIELD.className.name()).toString();
			}

			System.out.println(start + " " + rel.getType().name() + " " + cn + " : " + nodeToString(endNode));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.transit.dao.neo4j.IGraphDatabaseDAO#findNodeByField(me.transit.dao.neo4j.
	 * GraphDatabaseDAO.FIELD, java.lang.String)
	 */
	@Override
	public Node findNodeByField(FIELD fld, String key, Class<?> nodeClass) {

		Transaction tx = beginTransaction();
		Node rtn = null;

		try {
			rtn = graphDb.findNode(Label.label(nodeClass.getSimpleName()), fld.toString(), key.toString());
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip node to agency: ", ex);
		} finally {
			tx.close();
		}
		return rtn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.transit.dao.neo4j.IGraphDatabaseDAO#allRelationships(me.transit.dao.neo4j.
	 * GraphDatabaseDAO.REL_TYPES, org.neo4j.graphdb.Node)
	 */
	@Override
	public List<Node> allRelationships(REL_TYPES type, Node node) {
		return null;
	}

}
