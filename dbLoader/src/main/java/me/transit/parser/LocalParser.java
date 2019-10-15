package me.transit.parser;

import java.io.InputStream;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.database.neo4j.IGraphDatabaseDAO;
import me.factory.DaoBeanFactory;
import me.transit.parser.message.MessageAgency;
import me.transit.parser.message.ParserMessage;

public class LocalParser extends AbstractGTFSParser{


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

		IGraphDatabaseDAO graph = IGraphDatabaseDAO.class
				.cast(DaoBeanFactory.create().getDaoBean(IGraphDatabaseDAO.class));

		System.out.println("Num Coords: " + graph.getNumLocations());
		System.out.println("Num Found: " + graph.getFoundCount());
		System.out.println("Done");

	}

}
