package me.transit.parser.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;

import me.transit.parser.data.Blackboard;
import me.transit.omd.dao.LocationDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.message.MessageAgency;
import me.transit.parser.message.ParserMessage;

public class LocalParser extends AbstractGTFSParser {

	private final Log log = LogFactory.getLog(getClass().getName());
	private final IGraphDatabaseDAO graph;

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
				ParserMessage fr = new ObjectMapper().readValue(inStream, ParserMessage.class);

				for ( MessageAgency agency : fr.getAgencys()) {
					if ( this.doesExist(agency)) {
						parseFeeds(agency);
					} else {
					   log.warn("unable to find: " + agency);
					}
				}
			} catch (Exception e) {
				getLog().error(e.getLocalizedMessage());
			}
		}

		System.out.println("Done");

	}

}
