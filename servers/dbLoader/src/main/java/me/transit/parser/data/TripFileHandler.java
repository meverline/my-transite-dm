package me.transit.parser.data;

import lombok.extern.apachecommons.CommonsLog;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.RouteDao;
import me.transit.database.Route;
import me.transit.database.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.Map.Entry;

@Component(value="tripFileHandler")
@CommonsLog
public class TripFileHandler extends AbstractFileHandler {

	private final IGraphDatabaseDAO graphDatabase;
	private final RouteDao routeDao;
	
	/**
	 * 
	 * @param blackboard
	 */
	@Autowired
	public TripFileHandler(Blackboard blackboard, RouteDao routeDao, IGraphDatabaseDAO graphDatabase) {
		super(blackboard);
		this.routeDao = Objects.requireNonNull(routeDao, "routeDao can not be null");
		this.graphDatabase = Objects.requireNonNull(graphDatabase, "graphDatabase can not be null");
	}
	
	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "trips.txt";
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public boolean parse(String shapeFile) throws Exception {
		boolean rtn;
		Map<String, List<Trip>> routeToTrips = new HashMap<>();
		Set<String> invalidService = new HashSet<>();

		getBlackboard().getRouteShortName().clear();

		File fp = new File(shapeFile);
		if (!fp.exists()) {
			getBlackboard().getTripMap().clear();
			log.error("file does not exist: " + shapeFile);
			return false;
		}

		try (BufferedReader inStream = new BufferedReader(new FileReader(shapeFile))){

			if (!inStream.ready()) {
				getBlackboard().getTripMap().clear();
				log.error("file is empty: " + shapeFile);
				return false;
			}

			List<String> header = new ArrayList<>();
			Map<String, Integer> indexMap = processHeader(inStream.readLine(), header);
			log.info(header);

			String routeId = null;
			while (inStream.ready()) {

				String line = inStream.readLine();
				String [] data = line.split(",");

				Trip trip = new Trip();

				try {
					if (indexMap.containsKey("route_id")) {
						routeId = data[indexMap.get("route_id")].replace('"', ' ').trim();

						if (!routeToTrips.containsKey(routeId)) {
							routeToTrips.put(routeId, new ArrayList<>());
						}
					}

					if (indexMap.containsKey("trip_id")) {
						String id = data[indexMap.get("trip_id")].trim();
						trip.setId(id);
					}

					if (indexMap.containsKey("service_id")) {
						String id = data[indexMap.get("service_id")].trim();

						if (getBlackboard().getService().get(id) != null) {
							trip.setService(getBlackboard().getService().get(id));
						} else {
							if (!invalidService.contains(id)) {
								log.error("Invalid trip service id: " + id + " route: " + routeId);
								invalidService.add(id);
							}
						}
					}

					if (indexMap.containsKey("trip_headsign")) {
						String headSign = data[indexMap.get("trip_headsign")].trim();
						trip.setHeadSign(headSign.replaceAll("\"", " ").trim());
					}

					if (indexMap.containsKey("direction_id")) {
						int id = Integer.parseInt(data[indexMap.get("direction_id")].trim());
						try {
							Trip.DirectionType[] type = Trip.DirectionType.values();
							trip.setDirectionId(type[id]);
						} catch (Exception e) {
							log.warn("invalid direction_id: " + Integer.toString(id));
						}
					}

					if (indexMap.containsKey("shape_id") && data.length > indexMap.get("shape_id")) {
						String id = data[indexMap.get("shape_id")].trim();
						trip.setShape(getBlackboard().getShapes().get(id));
					}

					List<Trip> tripList = routeToTrips.get(routeId);
					Trip tripToUse = trip;
					boolean found = false;
					for (Trip item : tripList) {
						if (item.equals(trip)) {
							tripToUse = item;
							found = true;
							break;
						}
					}

					if (!found) {
						tripList.add(trip);
					}

					getBlackboard().getTripMap().put(trip.getId(), new RouteTripPair(routeId, tripToUse));

				} catch ( Exception ex ) {
					log.error(ex.getLocalizedMessage() + " : " + line + " : " + indexMap.toString());
				}
			}

			for (Entry<String, List<Trip>> data : routeToTrips.entrySet()) {

				Route route = routeDao.loadById(data.getKey(), getBlackboard().getAgencyName());
				int ndx = 0;
				for ( Trip trip : data.getValue()) {
					trip.setRouteTripIndex(ndx);
					route.getTripList().add(trip);
					try {
						graphDatabase.createRelationShip(route, trip);
					} catch (Exception e) {
						log.error(e.getLocalizedMessage() +
									" Route "+ route.getRouteId() +
									" Trip " + trip.getId());
					}
					ndx++;
				}
				
				routeDao.save(route);
				getBlackboard().getRouteShortName().put(data.getKey(), route.getShortName());
			}
			
			rtn = true;

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			rtn = false;
		}

		return rtn;
	}

}
