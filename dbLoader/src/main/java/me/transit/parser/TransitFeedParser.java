package me.transit.parser;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class TransitFeedParser {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) {
		Log log = LogFactory.getLog(TransitFeedParser.class.getName());
		
		AbstractGTFSParser parser = null;
		
		try {
			if (args.length > 0 &&  args[0].equals("service")) {
				parser = new ParserService();
			} else {
				parser = new LocalParser();
			}
		
			parser.start();
			
		} catch ( Exception ex ) {
			log.error(ex.getLocalizedMessage(), ex);
		}

	}

}
