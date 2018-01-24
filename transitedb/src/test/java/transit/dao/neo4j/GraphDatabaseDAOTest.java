package transit.dao.neo4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.Node;

import me.transit.dao.neo4j.GraphDatabaseDAO;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;

public class GraphDatabaseDAOTest {

	@Ignore
	@Test
	public void testfindRoutes() {
		
		String stopIds[] = { "12709", "12710", "9251" };
		Agency metro = new Agency("METRO");
		TransitStop stop = new TransitStop();
		
		stop.setAgency(metro);
		
		GraphDatabaseDAO graph = GraphDatabaseDAO.instance();
		
		for ( String id : stopIds ) {
			
			stop.setId(id);
			List<RouteStopData> data = graph.findRoutes(stop);
			
			assertNotNull(data);
			assertNotEquals(0, data.size());
			
			for ( RouteStopData item : data ) {
				assertNotNull( item.getRouteShortName());
				assertNotNull( item.getTripHeadSign());
				if ( item.getRouteShortName() != null ) {
					assertEquals( true, item.getRouteShortName().startsWith("4") );
				}
			}
		}
		
	}
	
	@Ignore
	@Test
	public void testFind() {
		
		String routeName[] = { "4B", "4E" };
		Agency metro = new Agency("METRO");
		Route route = new Route();
		
		route.setAgency(metro);
		
		GraphDatabaseDAO graph = GraphDatabaseDAO.instance();
		
		for ( String id : routeName ) {
			
			route.setShortName(id);
			Node data = graph.findNodeByField(GraphDatabaseDAO.FIELD.route, GraphDatabaseDAO.FIELD.route.makeKey(route));
			
			assertNotNull(data);
		}
		
	}
}
