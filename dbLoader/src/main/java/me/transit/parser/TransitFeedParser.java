package me.transit.parser;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.factory.DaoBeanFactory;
import me.transit.parser.data.FileHandlerFactory;


public class TransitFeedParser {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) {
		Log log = LogFactory.getLog(TransitFeedParser.class.getName());
		DaoBeanFactory theBeanFactory = DaoBeanFactory.initilize();
		
		AbstractGTFSParser parser = null;
		
		try {
			FileHandlerFactory factory = (FileHandlerFactory) theBeanFactory.getBean(FileHandlerFactory.class);
			if (args.length > 0 &&  args[0].equals("service")) {
				parser = new ParserService(factory);
			} else {
				parser = new LocalParser(factory);
			}
		
			parser.start();
			
		} catch ( Exception ex ) {
			log.error(ex.getLocalizedMessage(), ex);
		}

	}

}
