package me.transit.parser.data;

import java.sql.SQLException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.TransiteStopDao;
import me.transit.database.TransitStop;
import me.transit.parser.data.converters.DataConverterFactory;

@Component(value = "transitStopFileHandler")
public class TransitStopFileHandler extends AbstractDefaultFileHandler {

	private final TransiteStopDao transiteStopDao;

	@Autowired
	public TransitStopFileHandler(Blackboard blackboard, TransiteStopDao transiteStopDao,
			IGraphDatabaseDAO graphDatabase, DataConverterFactory dataConverterFactory) {
		super(blackboard, graphDatabase, dataConverterFactory);
		this.transiteStopDao = Objects.requireNonNull(transiteStopDao, "transiteStopDao can not be null");
	}
	
	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "stops.txt";
	}


	/**
	 * 
	 */
	@Override
	public void save(Object obj) throws SQLException {
		TransitStop stop = TransitStop.class.cast(obj);

		getBlackboard().getMBR().extend(stop.getLocation());

		if (stop.getDesc().isEmpty()) {
			stop.setDesc(stop.getName());
		}
		stop.setName(stop.getName().toLowerCase());
		transiteStopDao.save(stop);
		getGraphDatabase().addNode(stop);
	}
	
	
}
