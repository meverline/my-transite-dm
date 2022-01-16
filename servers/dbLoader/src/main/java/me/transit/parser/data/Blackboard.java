package me.transit.parser.data;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import me.math.kdtree.MinBoundingRectangle;
import me.transit.database.Agency;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.Trip;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value="blackboard")
@CommonsLog
@Getter
@Setter
public class Blackboard {

	private final Map<String, String> routeShortName = new HashMap<>();
	private final Map<String, RouteGeometry> shapes = new HashMap<>();
	private final Map<String, ServiceDate> service = new HashMap<>();
	private final Map<String, RouteTripPair> tripMap = new HashMap<>();
	private final Map<String, List<Trip>> routeToTrips = new HashMap<>();
	private final Map<String, Long> routeuuid = new HashMap<>();
	private final Map<String, Long> stopuuid = new HashMap<>();
	private Agency agency = null;
	private MinBoundingRectangle mbr = null;

	/**
	 * 
	 */
	public void reset() {
		this.setAgency(null);
		this.resetMBR();
		this.getRouteShortName().clear();
		this.getShapes().clear();
		this.getService().clear();
		this.getTripMap().clear();
		this.getRouteToTrips().clear();
	}

	/**
	 * 
	 * @return
	 */
	public MinBoundingRectangle getMBR() {
		if (mbr == null) {
			mbr = new MinBoundingRectangle();
		}
		return this.mbr;
	}

	/**
	 * 
	 */
	public void resetMBR() {
		mbr = new MinBoundingRectangle();
	}

	/**
	 * @return the agencyId
	 */
	public String getAgencyName() {
		return agency.getName();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ServiceDate getServiceDate(long id) {
		if (this.service.containsKey(Long.toString(id))) {
			return this.service.get(Long.toString(id));
		}
		log.info(this.getClass().getSimpleName() + " ServiceData not found: " + id);
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public RouteGeometry getShape(long id) {
		if (this.shapes.containsKey(Long.toString(id))) {
			return this.shapes.get(Long.toString(id));
		}
		log.info(this.getClass().getSimpleName() + " Shape not found: " + id);
		return null;
	}

}
