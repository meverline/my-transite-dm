package me.transit.parser.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.message.MessageAgency;
import me.transit.parser.omd.Feed;
import me.transit.parser.omd.Location;
import me.transit.parser.omd.OpenMobilityData;

public abstract class AbstractGTFSParser {

	private final Log log = LogFactory.getLog(getClass().getName());
	private final FileHandlerFactory factory;	
	private final OpenMobilityData dataFeed = new OpenMobilityData();
	private List<Location> locList = null;
	private final Properties properties = new Properties();
	/**
	 * 
	 */
	protected AbstractGTFSParser(FileHandlerFactory factory) {
		
		this.factory = Objects.requireNonNull(factory, "factory can not be null");
		
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
		
		String files[] = getProperties().get("order").toString().split(",");

		this.getFactory().reset();
		for (String dataFile : files) {
			getLog().info("parse: " + dataFile + " " + diretory);

			String key = dataFile.trim();
			this.getFactory().getHandler(key).parse(filePath(diretory, dataFile));
		}

		this.getFactory().getHandler(null).endProcess();
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

		System.out.println(agency.getFeed() + " RunTime: " + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(diff),
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
