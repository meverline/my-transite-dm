package me.database.neo4j;

import lombok.extern.apachecommons.CommonsLog;
import me.database.neo4j.visitors.AgencyVisitor;
import me.database.neo4j.visitors.RouteVisitor;
import me.database.neo4j.visitors.TransiteStopVisitor;
import me.database.neo4j.visitors.TripVisitor;
import me.transit.database.*;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@CommonsLog
public class GraphDatabaseDAO implements IGraphDatabaseDAO {
	
	private static final String HEAD_SIGN = "HeadSign";
	private static final String POINT = "Point";

	enum REL_TYPES implements RelationshipType {
		HAS_STOP, AGENCY, LOCATION, HAS_A, HEAD_SIGN
	}

	private static IGraphDatabaseDAO theOne = null;
	private final GraphDatabaseService graphDb;

	/**
	 * 
	 * @param dbPath
	 */
	private GraphDatabaseDAO(final String dbPath) {
		
		File dbDir = new File(dbPath);
		if (! dbDir.exists() ) {
			dbDir.mkdirs();
		}
		
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbDir).newGraphDatabase();

		registerShutdownHook(graphDb);
		indexDatabase();
	}

	/**
	 * 
	 * @param graphDb
	 */
	private void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			graphDb.shutdown();
		}));
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
    	indexMap.put(GraphDatabaseDAO.POINT, FIELD.coordinate.name());
    	indexMap.put(GraphDatabaseDAO.HEAD_SIGN, FIELD.trip_headSign.name());
    	
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
	public static synchronized IGraphDatabaseDAO instance(String dbPath) {
		if (theOne == null) {
			theOne = new GraphDatabaseDAO(dbPath);
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
	private Node addAgency(AgencyVisitor agency) {
		Map<String, String> properites = agency.getProperties(agency.getName());
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
		AgencyVisitor visitor = new AgencyVisitor(agency);
		Node node = null;
		Transaction tx = beginTransaction();
		try {
			node = graphDb.findNode(Label.label(Agency.class.getName()), FIELD.agency.name(), agency.getName());
			if (node == null) {
				node = this.addAgency(visitor);
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
		TransiteStopVisitor visitor = new TransiteStopVisitor(stop);
		Node node = null;
		Map<String, String> properites = visitor.getProperties(stop.getAgency().getName());

		Transaction tx = beginTransaction();
		try {
			
			node = graphDb.findNode(Label.label(stop.getClass().getSimpleName()),
													FIELD.stop.name(), visitor.makeKey(stop.getAgency().getName()));
			String key = visitor.makeCoordinateKey();
			Node coord = graphDb.findNode(Label.label(GraphDatabaseDAO.POINT),
												FIELD.coordinate.name(), key);

			if (node == null) {
				node = graphDb.createNode(Label.label(stop.getClass().getName()));
				for (Entry<String, String> entry : properites.entrySet()) {
					if (entry.getKey().equals( FIELD.coordinate.name()) ) {
						node.setProperty(entry.getKey(), entry.getValue());
					}
				}

				if (coord == null) {
					coord = graphDb.createNode(Label.label(GraphDatabaseDAO.POINT));
					coord.setProperty(FIELD.coordinate.name(), visitor.makeCoordinateKey());
				} 

				Relationship relationship = node.createRelationshipTo(coord, REL_TYPES.LOCATION);
				relationship.setProperty(FIELD.className.name(), this.getInterface(stop.getLocation()));

				createRelationShip(node, stop.getAgency());
			}
			tx.success();

		} catch (Exception ex) {
			tx.failure();
			logException("Error adding route: " + visitor.makeKey(stop.getAgency().getName()), ex);
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
		RouteVisitor visitor = new RouteVisitor(route);
		Map<String, String> properites = visitor.getProperties(route.getAgency().getName());
		Node node = null;
		Transaction tx = beginTransaction();
		try {
			node = graphDb.findNode(Label.label(Route.class.getSimpleName()),
										FIELD.route.name(), visitor.makeKey(route.getAgency().getName()));
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
			logException("Error adding route: " + visitor.makeKey(route.getAgency().getName()), ex);
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
	public Node addNode(Trip trip, Agency agency) {
		TripVisitor visitor = new TripVisitor(trip);
		Map<String, String> properites = visitor.getProperties(agency.getName());
		Node node = null;
		Transaction tx = beginTransaction();
		try {

			node = graphDb.findNode(Label.label(trip.getClass().getSimpleName()), 
											FIELD.trip.name(), visitor.makeKey(agency.getName()));
			Node hs = graphDb.findNode(Label.label(GraphDatabaseDAO.HEAD_SIGN), 
											FIELD.trip_headSign.name(), visitor.makeHeadSignKey(agency.getName()));

			if (node == null) {
				node = graphDb.createNode(Label.label(trip.getClass().getSimpleName()));
				for (Entry<String, String> entry : properites.entrySet()) {
					node.setProperty(entry.getKey(), entry.getValue());
				}

				createRelationShip(node, agency);
			}

			if (hs == null) {
				hs = graphDb.createNode(Label.label(GraphDatabaseDAO.HEAD_SIGN));
				hs.setProperty(FIELD.trip_headSign.name(), visitor.makeHeadSignKey(agency.getName()));
				createRelationShip(hs, agency);
			}

			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding trip: " + visitor.makeKey(agency.getName()), ex);
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
		TripVisitor tripVisitor = new TripVisitor(fromTrip);
		TransiteStopVisitor stopVisitor = new TransiteStopVisitor(toStop);

		Transaction tx = beginTransaction();
		try {

			Node to = graphDb.findNode(Label.label(toStop.getClass().getSimpleName()),
												FIELD.stop.name(), stopVisitor.makeKey(toStop.getAgency().getName()));
			if (to == null) {
				to = this.addNode(toStop);
			}

			Node from = graphDb.findNode(Label.label(fromTrip.getClass().getSimpleName()),
											FIELD.trip.name(), tripVisitor.makeKey(toStop.getAgency().getName()));
			if (from == null) {
				from = this.addNode(fromTrip, toStop.getAgency());
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
		RouteVisitor routeVisitor = new RouteVisitor(fromRoute);
		TransiteStopVisitor stopVisitor = new TransiteStopVisitor(toStop);

		Transaction tx = beginTransaction();
		try {
			Node to = graphDb.findNode(Label.label(toStop.getClass().getSimpleName()),
											FIELD.stop.name(), stopVisitor.makeKey(toStop.getAgency().getName()));
			if (to == null) {
				to = this.addNode(toStop);
			}

			Node from = graphDb.findNode(Label.label(fromRoute.getClass().getSimpleName()),
											FIELD.route.name(), routeVisitor.makeKey(toStop.getAgency().getName()));
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
		RouteVisitor routeVisitor = new RouteVisitor(fromRoute);
		TripVisitor tripVisitor = new TripVisitor(toTrip);
		Transaction tx = beginTransaction();
		try {
			Node to = graphDb.findNode(Label.label(toTrip.getClass().getSimpleName()), 
											FIELD.trip.name(), tripVisitor.makeKey(fromRoute.getAgency().getName()));
			if (to == null) {
				to = this.addNode(toTrip, fromRoute.getAgency());
			}

			Node from = graphDb.findNode(Label.label(fromRoute.getClass().getSimpleName()),
					                     FIELD.route.name(), routeVisitor.makeKey(fromRoute.getAgency().getName()));
			if (from == null) {
				from = this.addNode(fromRoute);
			}

			Node tohs = graphDb.findNode(Label.label(GraphDatabaseDAO.HEAD_SIGN), FIELD.trip_headSign.name(),
					tripVisitor.makeHeadSignKey(fromRoute.getAgency().getName()));

			if (to == null || from == null) {
				log.warn("Warng adding relationsip route to trip: nodes null: " + from + " to " + to);
				tx.success();
				return false;
			}

			Relationship relationship = from.createRelationshipTo(to, REL_TYPES.HAS_A);
			relationship.setProperty(FIELD.className.name(), this.getInterface(fromRoute));

			if (tohs == null || from == null) {
				log.warn("Warng adding HeadSign relationsip route to trip: nodes null: " + from + " to " + tohs);
				tx.success();
				return false;
			}

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
	private void createRelationShip(Node from, Agency toAgency) {
		AgencyVisitor visitor = new AgencyVisitor(toAgency);
		Transaction tx = beginTransaction();
		try {
			Node to = graphDb.findNode(Label.label(toAgency.getClass().getName()), 
											FIELD.agency.name(), toAgency.getName());
			if (to == null) {
				to = this.addAgency(visitor);
			}

			from.createRelationshipTo(to, REL_TYPES.AGENCY);
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip node to agency: ", ex);
		} finally {
			tx.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.dao.neo4j.IGraphDatabaseDAO#findRoutes(me.transit.database.
	 * TransitStop)
	 */
	@Override
	public List<RouteStopData> findRoutes(TransitStop stop) {
		TransiteStopVisitor visitor = new TransiteStopVisitor(stop);
		List<RouteStopData> rtn = new ArrayList<>();
		Transaction tx = beginTransaction();
		try {
			Node stopNode = graphDb.findNode(Label.label(stop.getClass().getSimpleName()), 
												FIELD.stop.name(), visitor.makeKey(stop.getAgency().getName()));
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
			rtn = graphDb.findNode(Label.label(nodeClass.getSimpleName()), fld.toString(), key);
			tx.success();
		} catch (Exception ex) {
			tx.failure();
			logException("Error adding relationsip node to agency: ", ex);
		} finally {
			tx.close();
		}
		return rtn;
	}

}
