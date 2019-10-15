package me.transit.parser;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.factory.DaoBeanFactory;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.DefaultFileHandler;
import me.transit.parser.data.FileHandler;
import me.transit.parser.data.ServiceDateFileHandler;
import me.transit.parser.data.ShapeFileHandler;
import me.transit.parser.data.StopTimeFileHandler;
import me.transit.parser.data.TripFileHandler;
import me.transit.parser.message.MessageAgency;
import me.transit.parser.omd.Feed;
import me.transit.parser.omd.Location;
import me.transit.parser.omd.OpenMobilityData;

public abstract class AbstractGTFSParser {

	private final Log log = LogFactory.getLog(getClass().getName());
	private final Map<String, FileHandler> handlers = new HashMap<>();
	private final Blackboard blackboard = new Blackboard();
	private final DefaultFileHandler defaultHandler;
	private final OpenMobilityData dataFeed = new OpenMobilityData();
	private List<Location> locList = null;

	/**
	 * 
	 */
	public AbstractGTFSParser() {
		DaoBeanFactory.initilize();

		defaultHandler = new DefaultFileHandler(blackboard);
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
	protected Map<String, FileHandler> getHandlers() {
		return handlers;
	}

	/**
	 * @return the defaultHandler
	 */
	protected DefaultFileHandler getDefaultHandler() {
		return defaultHandler;
	}

	/**
	 * Create the filePath.
	 * 
	 * @param directory
	 * @param file
	 * @return
	 */
	private String filePath(String directory, String file) {
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
	protected void parse(String diretory) {
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
	 * 
	 */
	protected void parseFeeds(MessageAgency agency) {

		long start = System.currentTimeMillis();
		for (Location loc : getLocList()) {

			if (loc.getTitle().equals(agency.getLocation())) {

				Map<String, Feed> feeds = getDataFeed().getFeeds(loc.getPid());
				if (feeds.containsKey(agency.getFeed())) {
					String path = dataFeed.download(feeds.get(agency.getFeed()));

					File check = new File(path, "agency.txt");
					if (check.exists()) {
						this.getLog().info(path + " ... ");
						this.parse(path);
					}

				}

			}
		}

		long end = System.currentTimeMillis();

		long diff = end - start;

		System.out.println("RunTime: " + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(diff),
				TimeUnit.MILLISECONDS.toSeconds(diff)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))));

	}
	
	/**
	 * 
	 * @return
	 */
    protected List<Location> getLocList() {
    	if ( locList == null ) {
    		locList = this.getDataFeed().getLocations();
    	}
		return locList;
	}

    /**
     * 
     * @return
     */
	protected OpenMobilityData getDataFeed() {
		return dataFeed;
	}


	/**
	 * 
	 */
	public abstract void start();

}
