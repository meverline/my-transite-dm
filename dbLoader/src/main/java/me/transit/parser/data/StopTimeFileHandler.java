package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.database.mongo.IDocumentDao;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.dao.TransiteStopDao;
import me.transit.database.Route;
import me.transit.database.StopTime;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

@Component(value="stopTimeFileHandler")
public class StopTimeFileHandler extends FileHandler {

	private Log log = LogFactory.getLog(getClass().getName());
	private IDocumentDao documentDao;
	private IGraphDatabaseDAO graphdb;
	private RouteDao routeDao;
	private TransiteStopDao transiteStopDao;

	/**
	 * 
	 * @param blackboard
	 */
	@Autowired
	public StopTimeFileHandler(RouteDao routeDao, TransiteStopDao transiteStopDao, 
							   IDocumentDao documentDao, IGraphDatabaseDAO graphDatabase,  
							   Blackboard blackboard) {
		super(blackboard);
		this.documentDao = documentDao;
		this.graphdb = graphDatabase;
		this.routeDao = routeDao;
		this.transiteStopDao = transiteStopDao;
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
	 * @param stopMap
	 * @return
	 */
	private void xrefStopToRoutes(HashMap<String, List<Trip>> tripMap) {

		try {

			for (Entry<String, List<Trip>> entry : tripMap.entrySet()) {
				Map<String, TransitStop> stopIds = new HashMap<String, TransitStop>();

				Route route = this.routeDao.loadById(entry.getKey(), getBlackboard().getAgencyName());
				for (Trip trip : entry.getValue()) {
					for (StopTime info : trip.getStopTimes()) {

						if (!stopIds.containsKey(info.getStopId())) {
							TransitStop stop = this.transiteStopDao.loadById(info.getStopId(), getBlackboard().getAgencyName());
							info.setStopName(stop.getName());

							Double[] location = new Double[2];
							location[0] = stop.getLocation().getCoordinate().x;
							location[1] = stop.getLocation().getCoordinate().y;
							info.setLocation(location);

							stopIds.put(info.getStopId(), stop);
						}
						graphdb.createRelationShip(trip, stopIds.get(info.getStopId()));
					}
				}

				for (TransitStop stopInfo : stopIds.values()) {
					graphdb.createRelationShip(route, stopInfo);
				}

				Map<String, Object> data = route.toDocument();
				data.put(Route.TRIPLIST, entry.getValue());
				this.documentDao.add(data);

			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public void parse(String shapeFile) {
		try {

			File fp = new File(shapeFile);
			if (!fp.exists()) {
				return;
			}

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if (!inStream.ready()) {
				inStream.close();
				return;
			}

			List<String> header = new ArrayList<String>();
			Map<String, Integer> indexMap = processHeader(inStream.readLine(), "shape", header);

			long lineCnt = 0;
			long cnt = 0;

			RouteTripPair trip = null;
			StopTime stopTime = null;

			while (inStream.ready()) {

				Boolean newStop = false;
				String line = inStream.readLine();
				if (line.trim().length() > 0 && line.indexOf(',') != -1) {
					String data[] = line.split(",");

					String id = data[indexMap.get("TripId")].replace('"', ' ').trim();
					trip = getBlackboard().getTripMap().get(id);

					if (indexMap.containsKey("StopId") && data[indexMap.get("StopId")] != null) {
						String stopId = data[indexMap.get("StopId")].replace('"', ' ').trim();
						stopTime = trip.getTrip().findStopTimeById(stopId);
						if (stopTime == null) {
							stopTime = new StopTime();
							stopTime.setStopId(stopId);
							newStop = true;
							trip.getTrip().addStopTime(stopTime);
						}

					}

					if (indexMap.containsKey("ArrivalTime") && data[indexMap.get("ArrivalTime")] != null) {
						String time[] = data[indexMap.get("ArrivalTime")].trim().split(":");
						StringBuilder builder = new StringBuilder();
						for (String str : time) {
							builder.append(str);
						}
						if (builder.toString().length() > 0) {
							stopTime.addArrivalTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));
						}
					}

					if (!newStop) {
						if (indexMap.containsKey("DropOffType")) {
							try {
								int ndx = Integer.parseInt(data[indexMap.get("DropOffType")].replace('"', ' ').trim());
								stopTime.setDropOffType(StopTime.PickupType.values()[ndx]);
							} catch (Exception ex) {
								log.error("Unknown Dropoff Type: ");
							}
						}

						if (indexMap.containsKey("PickupType")) {
							try {
								int ndx = Integer.parseInt(data[indexMap.get("PickupType")].replace('"', ' ').trim());
								stopTime.setPickupType(StopTime.PickupType.values()[ndx]);
							} catch (Exception ex) {
								log.error("Unknown Pickup Type: ");
							}
						}

						if (indexMap.containsKey("StopHeadSign")) {
							stopTime.setStopHeadSign(data[indexMap.get("StopHeadSign")].trim());
						}
					}

					lineCnt++;
					cnt++;
					if (cnt > 100000) {
						log.info("parseStopTimes " + lineCnt + " ...");
						cnt = 0;
					}
				}
			}

			inStream.close();

			log.info("parseStopTimes:build routeToTrip map " + getBlackboard().getTripMap().size());
			HashMap<String, List<Trip>> routeToTrips = new HashMap<String, List<Trip>>();
			for (RouteTripPair pair : getBlackboard().getTripMap().values()) {
				if (!routeToTrips.containsKey(pair.getRouteId())) {
					routeToTrips.put(pair.getRouteId(), new ArrayList<Trip>());
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

	}

}
