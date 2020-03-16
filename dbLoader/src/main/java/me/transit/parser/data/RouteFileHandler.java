package me.transit.parser.data;

import java.sql.SQLException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.database.Route;
import me.transit.parser.data.converters.DataConverterFactory;

@Component(value = "routeFileHandler")
public class RouteFileHandler extends AbstractDefaultFileHandler {

	private final RouteDao routeDao;

	@Autowired
	public RouteFileHandler(Blackboard blackboard, RouteDao routeDao,
			IGraphDatabaseDAO graphDatabase, DataConverterFactory dataConverterFactory) {
		super(blackboard, graphDatabase, dataConverterFactory);
		this.routeDao = Objects.requireNonNull(routeDao,"routeDao can not be null");
	}
	
	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "routes.txt";
	}

	/**
	 * 
	 */
	@Override
	public void save(Object obj) throws SQLException {
		Route route = Route.class.cast(obj);
		route.setAgency(this.getBlackboard().getAgency());
		if (route.getShortName() == null || route.getShortName().isEmpty()) {
			route.setShortName(route.getLongName());
		}
		
		Route data = routeDao.save(route);
		this.getBlackboard().getRouteuuid().put(data.getId(),  data.getUUID());
		getGraphDatabase().addNode(route);
	}
}
