package me.transit.parser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.omd.dao.LocationDao;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.message.ParserMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;

@CommonsLog
public class LocalParser extends AbstractGTFSParser {

	@SuppressWarnings("unused")
	private final IGraphDatabaseDAO graph;

	/**
	 *
	 * @param factory
	 * @param graphDatabase
	 * @param locationDao
	 * @param blackboard
	 */
	@Autowired
	public LocalParser(FileHandlerFactory factory, IGraphDatabaseDAO graphDatabase, LocationDao locationDao, Blackboard blackboard) {
		super(factory, locationDao, blackboard);
		this.graph = Objects.requireNonNull(graphDatabase, "graphDatabase can not be null");
	}

	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void start() {

		InputStream inStream = getClass().getClassLoader().getResourceAsStream("agencys.json");
		
		if (inStream != null) {
			try {
				this.messageProcessor( new ObjectMapper().readValue(inStream, ParserMessage.class));
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}

		System.out.println("Done");

	}

	public void stop()
	{

	}


}
