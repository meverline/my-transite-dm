package transit.dao.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import me.database.mongo.DocumentDao;
import me.database.mongo.IDocument;
import me.database.mongo.IDocumentDao;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.StopTime;
import me.transit.database.Trip;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MongoDBTest {

	@Test
	public void testtoDocField() {

		List<String> fields = new ArrayList<String>();

		fields.add(Agency.AGENCY);
		assertEquals(Agency.AGENCY, DocumentDao.toDocField(fields));

		fields.clear();
		fields.add(Route.TRIPLIST);
		fields.add(Trip.STOPTIMES);
		fields.add(StopTime.STOPNAME);

		String tmp = DocumentDao.toDocField(fields);

		assertEquals(Route.TRIPLIST + "." + Trip.STOPTIMES + "." + StopTime.STOPNAME, tmp);
	}

	@Test
	public void testCount() {

		IDocumentDao dao;
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

		String tmp = DocumentDao.toDocField(fields);

		List<IQueryTuple> query = new ArrayList<IQueryTuple>();

		query.add(new StringTuple(tmp, "n barton", StringTuple.MATCH.END));

		IDocumentDao dao;
		try {
			dao = DocumentDao.instance();
			List<IDocument> routes = dao.find(query);

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

		String tmp = DocumentDao.toDocField(fields);

		List<IQueryTuple> query = new ArrayList<IQueryTuple>();

		query.add(new StringTuple(tmp, "4B", StringTuple.MATCH.EXACT));

		IDocumentDao dao;
		try {
			dao = DocumentDao.instance();
			List<IDocument> routes = dao.find(query);

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

		IDocumentDao dao;
		try {
			dao = DocumentDao.instance();
			List<IDocument> routes = dao.find(query);

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
