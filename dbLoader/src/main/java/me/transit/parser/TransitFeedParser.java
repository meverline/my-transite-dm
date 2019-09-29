package me.transit.parser;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.database.neo4j.IGraphDatabaseDAO;
import me.factory.DaoBeanFactory;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.DefaultFileHandler;
import me.transit.parser.data.FileHandler;
import me.transit.parser.data.ServiceDateFileHandler;
import me.transit.parser.data.ShapeFileHandler;
import me.transit.parser.data.StopTimeFileHandler;
import me.transit.parser.data.TripFileHandler;

/**
 * 
 * @author markeverline
 *
 */
public class TransitFeedParser {

	private final Log log = LogFactory.getLog(getClass().getName());
	private final Map<String, FileHandler> handlers = new HashMap<>();
	private final Blackboard blackboard = new Blackboard();
	private final DefaultFileHandler defaultHandler;

	/**
	 * 
	 */
	public TransitFeedParser(String path) {
		DaoBeanFactory.initilize();

		defaultHandler = new DefaultFileHandler(path, blackboard);
		handlers.put("shapes.txt", new ShapeFileHandler(blackboard));
		handlers.put("calendar.txt", new ServiceDateFileHandler(blackboard));
		handlers.put("stop_times.txt", new StopTimeFileHandler(blackboard));
		handlers.put("trips.txt", new TripFileHandler(blackboard));
	}

	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @return the blackboard
	 */
	protected Blackboard getBlackboard() {
		return blackboard;
	}

	/**
	 * @return the handlers
	 */
	public Map<String, FileHandler> getHandlers() {
		return handlers;
	}

	/**
	 * @return the defaultHandler
	 */
	public DefaultFileHandler getDefaultHandler() {
		return defaultHandler;
	}

	/**
	 * Create the filePath.
	 * 
	 * @param directory
	 * @param file
	 * @return
	 */
	protected String filePath(String directory, String file) {
		StringBuilder rtn = new StringBuilder();

		rtn.append(directory.trim());
		rtn.append("/");
		rtn.append(file.trim());
		return rtn.toString();
	}

	/**
	 * The directory where the data file are located at.
	 * 
	 * @param diretory
	 */
	public void parse(String diretory) {
		String files[] = getDefaultHandler().getProperties().get("order").toString().split(",");

		this.getBlackboard().reset();
		for (String dataFile : files) {
			getLog().info("parse: " + dataFile + " " + diretory);

			String key = dataFile.trim();
			if (getHandlers().containsKey(key)) {
				getHandlers().get(key).parse(filePath(diretory, dataFile));
			} else {
				getDefaultHandler().parse(filePath(diretory, dataFile));
			}
		}

		getBlackboard().getAgency().setMBR(getBlackboard().getMBR().toPolygon());
		log.info(getBlackboard().getMBR().toString());
		try {
			getDefaultHandler().save(getBlackboard().getAgency());
		} catch (SQLException e) {
			getLog().error(e);
		}
		getLog().info("done processing: " + diretory);
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) {

		String path = "/Users/markeverline/Perforce/Transite";
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			path = "C:/java/me-transite/my-transite-dm";
		}

		if (args.length > 0) {
			path = args[0];
		}

		TransitFeedParser feedParser = new TransitFeedParser(path);

		File fp = new File(path, "google_transit");

		long start = System.currentTimeMillis();
		for (File dir : fp.listFiles()) {
			File check = new File(dir, "agency.txt");
			if (check.exists()) {
				feedParser.getLog().info(dir.getAbsolutePath() + " ... ");
				feedParser.parse(dir.getAbsolutePath());
			}
		}

		long end = System.currentTimeMillis();

		long diff = end - start;

		System.out.println("RunTime: " + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(diff),
				TimeUnit.MILLISECONDS.toSeconds(diff)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))));

		IGraphDatabaseDAO graph = IGraphDatabaseDAO.class
				.cast(DaoBeanFactory.create().getDaoBean(IGraphDatabaseDAO.class));

		System.out.println("Num Coords: " + graph.getNumLocations());
		System.out.println("Num Found: " + graph.getFoundCount());
		System.out.println("Done");

	}

}
