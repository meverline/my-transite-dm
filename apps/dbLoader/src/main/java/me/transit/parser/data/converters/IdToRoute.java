package me.transit.parser.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.transit.dao.RouteDao;
import me.transit.database.Route;

@Service(value="idToRoute")
public class IdToRoute extends DataConverter {
	
	private final RouteDao dao;
	
	@Autowired
	public IdToRoute(RouteDao routeDao, DataConverterFactory dataConverterFactory) {
		this.dao = routeDao;
		dataConverterFactory.register(this);
	}
	
	public IdToRoute(RouteDao routeDao) {
		this.dao = routeDao;
	}
	
	@Override
	public Class<?> getType() {
		return Route.class;
	}
	
	@Override
	public Object convert(String data) {
		String id = data.trim();
		return dao.loadById(id, this.getBlackboard().getAgencyName());
	}

	@Override
	public DataConverter clone() {
		return new IdToRoute(dao);
	}
	
	

}
