package me.transit.parser;

import java.io.InputStream;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.message.MessageAgency;
import me.transit.parser.message.ParserMessage;

public class LocalParser extends AbstractGTFSParser {
	
	IGraphDatabaseDAO graph;

	@Autowired
	public LocalParser(FileHandlerFactory factory, IGraphDatabaseDAO graphDatabase) {
		super(factory);
		this.graph = graphDatabase;
		
	}
	/**
	 * @param args
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
				getLog().error(e.getLocalizedMessage(), e);
			}
		}


		System.out.println("Num Coords: " + graph.getNumLocations());
		System.out.println("Num Found: " + graph.getFoundCount());
		System.out.println("Done");

	}

}
