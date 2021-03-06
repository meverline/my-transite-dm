package me.datamining.metric;

import org.springframework.stereotype.Service;

import me.database.nsstore.DocumentSession;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;

@Service(value="daoProvider")
public class DaoProvider {
	
	private final RouteDao routeDao;
	private final IGraphDatabaseDAO graphDatabase;
	private final DocumentSession documentDao;
	
	/**
	 * 
	 * @param routeDao
	 * @param graphDatabase
	 * @param documentDao
	 */
	public DaoProvider(RouteDao routeDao, IGraphDatabaseDAO graphDatabase, DocumentSession documentDao) {
		this.routeDao = routeDao;
		this.graphDatabase = graphDatabase;
		this.documentDao = documentDao;
	}

	/**
	 * @return the routeDao
	 */
	protected RouteDao getRouteDao() {
		return routeDao;
	}

	/**
	 * @return the graphDatabase
	 */
	protected IGraphDatabaseDAO getGraphDatabase() {
		return graphDatabase;
	}

	/**
	 * @return the documentDao
	 */
	protected DocumentSession getDocumentDao() {
		return documentDao;
	}
	
}
