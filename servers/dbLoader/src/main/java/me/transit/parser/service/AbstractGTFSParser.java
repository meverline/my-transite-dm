package me.transit.parser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.transit.omd.dao.LocationDao;
import me.transit.omd.data.Feed;
import me.transit.omd.data.Location;
import me.transit.omd.data.OpenMobilityData;
import me.transit.parser.data.Blackboard;
import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.message.MessageAgency;
import me.transit.parser.message.ParserMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGTFSParser {

	private final Log log = LogFactory.getLog(getClass().getName());
	private final FileHandlerFactory factory;	
	private final OpenMobilityData dataFeed = new OpenMobilityData();
	private List<Location> locList = null;
	private final Properties properties = new Properties();
	private final LocationDao locationDao;
	private final Blackboard blackboard;
	/**
	 * 
	 */
	protected AbstractGTFSParser(FileHandlerFactory factory, LocationDao locationDao, Blackboard blackboard) {
		
		this.factory = Objects.requireNonNull(factory, "factory can not be null");
		this.locationDao = Objects.requireNonNull(locationDao, "locationDao can not be null");
		this.blackboard = Objects.requireNonNull(blackboard, "blackboard can not be null");
		
		try {
			InputStream inStream =  getClass().getClassLoader().getResourceAsStream("ClassMap.properties");
			getProperties().load(inStream);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}


	}
	
	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @return the defaultHandler
	 */
	protected FileHandlerFactory getFactory() {
		return factory;
	}

	/**
	 * Create the filePath.
	 * 
	 * @param directory
	 * @param file
	 * @return
	 */
	private String filePath(String directory, String file) {
		return directory.trim() + "/"+ file.trim();
	}

	/**
	 * The directory where the data file are located at.
	 * 
	 * @param diretory
	 * @throws Exception 
	 */
	protected void parse(String diretory) throws Exception {
		
		String [] files = getProperties().get("order").toString().split(",");

		this.blackboard.reset();
		for (String dataFile : files) {
			getLog().info("parse: " + dataFile + " " + diretory);
			String key = dataFile.trim();
			this.getFactory().getHandler(key).parse(filePath(diretory, dataFile));
			
		}
		
		this.getFactory().getHandler("agency.txt").endProcess();
		getLog().info("done processing: " + diretory);
	}

	/**
	 *
	 * @param agency
	 * @param loc
	 * @throws Exception
	 */
	protected void parseLocation(MessageAgency agency, Location loc) throws Exception {
		final String dirRoot = "/Users/markeverline/tmp";
		Map<String, Feed> feeds = getDataFeed().getFeeds(loc.getPid());

		String agencyName = agency.getLocation().replace(',', '_').replace(' ', '_');
		File dir = new File(dirRoot);
		if ( ! dir.exists() ) {
			dir.mkdirs();
		}
		File fp = new File(dirRoot + "/Feeds_" + agencyName  + ".json");

		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(fp, feeds);

		if (feeds.containsKey(agency.getFeed())) {
			String path = dataFeed.download(feeds.get(agency.getFeed()));

			File check = new File(path, "agency.txt");
			if (check.exists()) {
				this.getLog().info(path + " ... ");
				this.parse(path);
			} else {
				log.warn("agency.txt not found for: " + loc + " pids found ");
			}

		}
	}

	/**
	 * @throws Exception 
	 * 
	 */
	protected void parseFeeds(MessageAgency agency) throws Exception {

		long start = System.currentTimeMillis();

		Location location = locationDao.findByTitle(agency.getLocation());
		if (location != null) {
			this.parseLocation(agency, location);
		} else {
			log.info("agency " + agency + " not found in database");
			for (Location loc : getLocList()) {

				if (locationDao.findById(loc.getId()) == null) {
					log.info("saving: " + loc);
					locationDao.save(loc);
				}

				if (loc.getTitle().equals(agency.getLocation())) {
					this.parseLocation(agency, loc);
				}
			}
		}
		long end = System.currentTimeMillis();

		long diff = end - start;

		log.info(agency.getFeed() + " RunTime: " + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(diff),
				TimeUnit.MILLISECONDS.toSeconds(diff)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))));

	}

	public boolean doesExist(MessageAgency agency) throws SQLException {

		Location location = locationDao.findByTitle(agency.getLocation());
		if (location != null) {
			return true;
		}
		else
		{
			for (Location loc : getLocList()) {
				if (locationDao.findById(loc.getId()) == null) {
					try {
						locationDao.save(loc);
					} catch (SQLException ex) {
						log.error("Unable to save location: " + loc + " error " + ex.getLocalizedMessage());
					}
				}

				if (loc.getTitle().equals(agency.getLocation())) {
					return  true;
				}
			}
		}

		return false;
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
	 * @param fr
	 */
	public void messageProcessor(ParserMessage fr) {
		try {
			for (MessageAgency agency : fr.getAgencys()) {
				if ( this.doesExist(agency)) {
					parseFeeds(agency);
				} else {
					log.warn("unable to find: " + agency);
				}
			}
		} catch (Exception ex) {
			this.getLog().error(ex.getLocalizedMessage(), ex);
		}
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

	/**
	 *
	 */
	public abstract void stop();

}
