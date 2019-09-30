package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.database.neo4j.IGraphDatabaseDAO;
import me.factory.DaoBeanFactory;
import me.transit.dao.RouteDao;
import me.transit.database.Route;
import me.transit.database.Trip;

public class TripFileHandler extends FileHandler {

	private Log log = LogFactory.getLog(getClass().getName());

	/**
	 * 
	 * @param blackboard
	 */
	public TripFileHandler(Blackboard blackboard) {
		super(blackboard);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public void parse(String shapeFile) {
		Map<String, List<Trip>> routeToTrips = new HashMap<String, List<Trip>>();
		Set<String> invalidService = new HashSet<String>();

		getBlackboard().getRouteShortName().clear();
		try {

			File fp = new File(shapeFile);
			if (!fp.exists()) {
				getBlackboard().getTripMap().clear();
			}

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if (!inStream.ready()) {
				inStream.close();
				getBlackboard().getTripMap().clear();
			}

			List<String> header = new ArrayList<String>();
			Map<String, Integer> indexMap = processHeader(inStream.readLine(), "trip", header);

			String routeId = null;
			while (inStream.ready()) {

				String line = inStream.readLine();
				String data[] = line.split(",");

				Trip trip = new Trip();

				trip.setAgency(getBlackboard().getAgency());

				if (indexMap.containsKey("RouteId")) {
					routeId = data[indexMap.get("RouteId")].replace('"', ' ').trim();

					if (!routeToTrips.containsKey(routeId)) {
						routeToTrips.put(routeId, new ArrayList<Trip>());
					}
				}

				if (indexMap.containsKey("Id")) {
					String id = data[indexMap.get("Id")].trim();
					trip.setId(id);
				}

				if (indexMap.containsKey("ServiceId")) {
					String id = data[indexMap.get("ServiceId")].trim();

					if (getBlackboard().getService().get(id) != null) {
						trip.setService(getBlackboard().getService().get(id));
					} else {
						if (!invalidService.contains(id)) {
							log.error("Invalid trip service id: " + id + " route: " + routeId);
							invalidService.add(id);
						}
					}
				}

				if (indexMap.containsKey("Headsign")) {
					trip.setHeadSign(data[indexMap.get("Headsign")].trim());
				}

				if (indexMap.containsKey("DirectionId")) {
					try {
						int id = Integer.parseInt(data[indexMap.get("DirectionId")].trim());
						Trip.DirectionType[] type = Trip.DirectionType.values();
						trip.setDirectionId(type[id]);
					} catch (Exception e) {
					}
				}

				if (indexMap.containsKey("ShapeId") && data.length > indexMap.get("ShapeId")) {
					String id = data[indexMap.get("ShapeId")].trim();
					trip.setShape(getBlackboard().getShaps().get(id));
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

				trip.setAgency(getBlackboard().getAgency());
				getBlackboard().getTripMap().put(trip.getId(), new RouteTripPair(routeId, tripToUse));
			}
			inStream.close();

			IGraphDatabaseDAO graphdb = IGraphDatabaseDAO.class
					.cast(DaoBeanFactory.create().getDaoBean(IGraphDatabaseDAO.class));

			RouteDao dao = RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));
			for (Entry<String, List<Trip>> data : routeToTrips.entrySet()) {

				Route route = dao.loadById(data.getKey(), getBlackboard().getAgencyName());
				route.setTripList(data.getValue());
				dao.save(route);
				getBlackboard().getRouteShortName().put(data.getKey(), route.getShortName());

				for (Trip entry : data.getValue()) {
					graphdb.createRelationShip(route, entry);
				}
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

	}

}