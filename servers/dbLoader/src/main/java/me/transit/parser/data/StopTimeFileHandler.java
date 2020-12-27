package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import me.transit.dao.RouteDocumentDao;
import me.transit.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.database.nsstore.DocumentSession;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.dao.TransiteStopDao;

@Component(value = "stopTimeFileHandler")
public class StopTimeFileHandler extends AbstractFileHandler {

	private Log log = LogFactory.getLog(getClass().getName());
	private RouteDocumentDao documentDao;
	private IGraphDatabaseDAO graphdb;
	private RouteDao routeDao;
	private TransiteStopDao transiteStopDao;

	/**
	 *
	 * @param routeDao
	 * @param transiteStopDao
	 * @param documentDao
	 * @param graphDatabase
	 * @param blackboard
	 */
	@Autowired
	public StopTimeFileHandler(RouteDao routeDao, TransiteStopDao transiteStopDao, RouteDocumentDao documentDao,
			IGraphDatabaseDAO graphDatabase, Blackboard blackboard) {
		super(blackboard);
		this.documentDao = Objects.requireNonNull(documentDao, "documentDao can not be null");
		this.graphdb = Objects.requireNonNull(graphDatabase, "graphDatabase can not be null");
		this.routeDao = Objects.requireNonNull(routeDao, "routeDao can not be null");
		this.transiteStopDao = Objects.requireNonNull(transiteStopDao, "transiteStopDao can not be null");
	}

	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "stop_times.txt";
	}

	/**
	 * 
	 * @param tripMap
	 */
	private void xrefStopToRoutes(HashMap<String, List<Trip>> tripMap) {

		for (Entry<String, List<Trip>> entry : tripMap.entrySet()) {
			Map<String, TransitStop> stopIds = new HashMap<>();
			try {
				Route route = this.routeDao.loadById(entry.getKey(), getBlackboard().getAgencyName());

				for (Trip trip : entry.getValue()) {
					for (StopTime info : trip.getStopTimes()) {

						if (!stopIds.containsKey(info.getStopId())) {
							TransitStop stop = this.transiteStopDao.loadById(info.getStopId(),
									getBlackboard().getAgencyName());
							info.setStopName(stop.getName());

							Double[] location = new Double[2];
							location[0] = stop.getLocation().getCoordinate().x;
							location[1] = stop.getLocation().getCoordinate().y;
							info.setLocation(location);

							stopIds.put(info.getStopId(), stop);
						}
						try {
							graphdb.createRelationShip(trip, stopIds.get(info.getStopId()));
						} catch (Exception ex) {
							log.error("Unable to add relationship: " + ex.getLocalizedMessage());
						}
					}
				}

				if (route == null) {
					log.warn(String.format("route is null for id: %s agency %s", entry.getKey(), getBlackboard().getAgencyName()));
				} else {
					for (TransitStop stopInfo : stopIds.values()) {
						try {
							graphdb.createRelationShip(route, stopInfo);
						} catch (Exception ex) {
							log.error("Unable to add relationship: " + ex.getLocalizedMessage());
						}
					}

					this.documentDao.add( new RouteDocument(route, entry.getValue()));
				}
			} catch (Exception e) {
				log.error(String.format("xrefStopToRoutes Trip map error Key %s Value %s", entry.getKey(), entry.getValue()));
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public boolean parse(String shapeFile) throws Exception {

		try {

			File fp = new File(shapeFile);
			if (!fp.exists()) {
				log.error("file does not exist: " + shapeFile);
				return false;
			}

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if (!inStream.ready()) {
				inStream.close();
				log.error("file is empty: " + shapeFile);
				return false;
			}

			List<String> header = new ArrayList<>();
			Map<String, Integer> indexMap = processHeader(inStream.readLine(), header);
			log.info(header);

			long lineCnt = 0;
			long cnt = 0;

			while (inStream.ready()) {

				boolean newStop = false;
				String line = inStream.readLine();
				if (line.trim().length() > 0 && line.indexOf(',') != -1) {
					String [] data = line.split(",");

					try {
						String id = data[indexMap.get("trip_id")].trim();
						RouteTripPair routeTripPair = getBlackboard().getTripMap().get(id);
						if (routeTripPair == null) {
							log.error("Unkonwn trip: " + id);
						} else {

							if (indexMap.containsKey("stop_id") && data[indexMap.get("stop_id")] != null) {
								String stopId = data[indexMap.get("stop_id")].replace('"', ' ').trim();
								StopTime stopTime = routeTripPair.getTrip().findStopTimeById(stopId);
								if (stopTime == null) {
									stopTime = new StopTime();
									stopTime.setStopId(stopId);
									newStop = true;
									routeTripPair.getTrip().addStopTime(stopTime);
								}


								if (indexMap.containsKey("arrival_time") && data[indexMap.get("arrival_time")] != null) {
									String[] time = data[indexMap.get("arrival_time")].trim().split(":");
									StringBuilder builder = new StringBuilder();
									for (String str : time) {
										builder.append(str);
									}
									if (builder.toString().length() > 0) {
										stopTime.addArrivalTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));
									}
								}

								if (!newStop) {
									if (indexMap.containsKey("drop_off_type")) {
										try {
											int ndx = Integer
													.parseInt(data[indexMap.get("drop_off_type")].replace('"', ' ').trim());
											stopTime.setDropOffType(StopTime.PickupType.values()[ndx]);
										} catch (Exception ex) {
											log.error("Unknown Dropoff Type: ");
										}
									}

									if (indexMap.containsKey("pickup_type")) {
										try {
											int ndx = Integer.parseInt(data[indexMap.get("pickup_type")].replace('"', ' ').trim());
											stopTime.setPickupType(StopTime.PickupType.values()[ndx]);
										} catch (Exception ex) {
											log.error("Unknown Pickup Type: ");
										}
									}

									if (indexMap.containsKey("stop_headsign")) {
										stopTime.setStopHeadSign(data[indexMap.get("stop_headsign")].trim());
									}

								}
							}
						}
						lineCnt++;
						cnt++;
						if (cnt > 10000) {
							log.info("parseStopTimes " + lineCnt + " ...");
							cnt = 0;
						}
					} catch ( Exception ex ) {
						log.error(ex.getLocalizedMessage() + " : " + line + " : " + indexMap.toString());
					}
				}
			}

			inStream.close();
			log.info("Found " + lineCnt + " stops");
			log.info("parseStopTimes:build routeToTrip map " + getBlackboard().getTripMap().size());
			HashMap<String, List<Trip>> routeToTrips = new HashMap<>();
			for (RouteTripPair pair : getBlackboard().getTripMap().values()) {
				if (!routeToTrips.containsKey(pair.getRouteId())) {
					routeToTrips.put(pair.getRouteId(), new ArrayList<>());
				}
				List<Trip> list = routeToTrips.get(pair.getRouteId());
				if (!list.contains(pair.getTrip())) {
					list.add(pair.getTrip());
				}
			}

			log.info("parseStopTimes:xrefStopToRoutes " + routeToTrips.size());
			this.xrefStopToRoutes(routeToTrips);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return true;

	}

}
