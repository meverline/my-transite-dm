package transit.dao.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import me.database.mongo.DocumentDao;
import me.database.nsstore.IDocument;
import me.database.nsstore.AbstractDocument;
import me.database.nsstore.IDocumentSession;
import me.database.nsstore.StoreUtils;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.StopTime;
import me.transit.database.Trip;
import me.transit.database.Trip.DirectionType;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MongoDBTest {
	
   

	@Test
	public void testtoDocField() {
		
		Trip trip = new Trip();
		StopTime st = new StopTime();
		
		Agency agency = new Agency();
		agency.setName("Agency");
		agency.setFareUrl("url");

		st.setStopHeadSign("Head Sign");
		st.setStopName("name");
		st.setStopId("537");
		st.setTripId("523");
		st.setDropOffType(StopTime.PickupType.COORDINATE);
		st.setPickupType(StopTime.PickupType.PHONE);

		for (int ndx = 0; ndx < 5; ndx++) {
			trip.addStopTime(st);
		}

		trip.setDirectionId(DirectionType.IN_BOUND);
		trip.setHeadSign("headSign");
	}

	@Test
	public void testCount() {

		IDocumentSession dao;
		try {
			dao = DocumentDao.instance();

			assertNotNull(dao);
			assertEquals(1248, dao.size());

		} catch (UnknownHostException e) {
			fail(e.getLocalizedMessage());

		}
	}

	@Test
	public void testFind() {

		List<String> fields = new ArrayList<String>();

		fields.clear();
		fields.add(Route.TRIPLIST);
		fields.add(Trip.STOPTIMES);
		fields.add(StopTime.STOPNAME);

		String tmp = StoreUtils.toDocField(fields);

		List<IQueryTuple> query = new ArrayList<IQueryTuple>();

		query.add(new StringTuple(tmp, "n barton", StringTuple.MATCH.END));

		IDocumentSession dao;
		try {
			dao = DocumentDao.instance();
			List<AbstractDocument> routes = dao.find(query);

			assertNotNull(routes);
			assertEquals(8, routes.size());

		} catch (UnknownHostException e) {
			fail(e.getLocalizedMessage());

		}

	}

	@Test
	public void testFindShortName() {

		List<String> fields = new ArrayList<String>();

		fields.clear();
		fields.add(Route.SHORTNAME);

		String tmp = StoreUtils.toDocField(fields);

		List<IQueryTuple> query = new ArrayList<IQueryTuple>();

		query.add(new StringTuple(tmp, "4B", StringTuple.MATCH.EXACT));

		IDocumentSession dao;
		try {
			dao = DocumentDao.instance();
			List<AbstractDocument> routes = dao.find(query);

			assertNotNull(routes);
			assertEquals(1, routes.size());

			Route route = Route.class.cast(routes.get(0));

			assertEquals("4B", route.getShortName());

		} catch (UnknownHostException e) {
			fail(e.getLocalizedMessage());

		}

	}

	@Test
	public void testList() {

		List<IQueryTuple> query = new ArrayList<IQueryTuple>();

		IDocumentSession dao;
		try {
			dao = DocumentDao.instance();
			List<AbstractDocument> routes = dao.find(query);

			assertNotNull(routes);
			assertEquals(dao.size(), routes.size());

			int total = 0;
			for (IDocument obj : routes) {
				if (obj instanceof Route) {
					total++;
				}
			}
			assertEquals(routes.size(), total);

		} catch (UnknownHostException e) {
			fail(e.getLocalizedMessage());

		}

	}
}
