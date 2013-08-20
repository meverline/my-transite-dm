package me.transit.dao.neo4j;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import me.transit.database.Agency;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;
import me.transit.database.impl.AgencyImpl;
import me.transit.database.impl.TransitStopImpl;

import org.junit.Test;

public class GraphDatabaseDAOTest {

	@Test
	public void testfindRoutes() {
		
		String stopIds[] = { "12709", "12710", "9251" };
		Agency metro = new AgencyImpl("METRO");
		TransitStop stop = new TransitStopImpl();
		
		stop.setAgency(metro);
		
		GraphDatabaseDAO graph = GraphDatabaseDAO.instance();
		
		for ( String id : stopIds ) {
			
			stop.setId(id);
			List<RouteStopData> data = graph.findRoutes(stop);
			
			assertNotNull(data);
			assertNotEquals(0, data.size());
			
			for ( RouteStopData item : data ) {
				assertEquals( true, item.getRouteShortName().startsWith("4"));
			}
		}
		
	}
}
