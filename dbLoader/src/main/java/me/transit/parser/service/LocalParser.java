package me.transit.parser.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;

import me.transit.parser.omd.dao.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.message.MessageAgency;
import me.transit.parser.message.ParserMessage;

public class LocalParser extends AbstractGTFSParser {
	
	IGraphDatabaseDAO graph;

	@Autowired
	public LocalParser(FileHandlerFactory factory, IGraphDatabaseDAO graphDatabase, LocationDao locationDao) {
		super(factory, locationDao);
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
					parseFeeds(agency);
				}
			} catch (Exception e) {
				getLog().error(e.getLocalizedMessage());
			}
		}

		System.out.println("Done");

	}

}
