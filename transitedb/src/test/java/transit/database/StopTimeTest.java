package transit.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import me.transit.database.StopTime;

public class StopTimeTest extends AbstractDatabaseTest {

	/**
	 * 
	 */
	@Test
	public void testConstructor() {

		StopTime st = new StopTime();

		assertNotNull(st.getArrivalTime());
		assertEquals(StopTime.PickupType.REGULAR, st.getPickupType());
		assertEquals(StopTime.PickupType.REGULAR, st.getDropOffType());
		assertEquals("", st.getStopHeadSign());
		assertEquals("", st.getStopName());
		assertNull(st.getStopId());
		assertNull(st.getTripId());
	}

	@Test
	public void testJson() throws JsonProcessingException {

		StopTime object = new StopTime();

		object.setStopHeadSign("Head Sign");
		object.setStopName("name");
		object.setStopId("537");
		object.setTripId("523");
		object.setDropOffType(StopTime.PickupType.COORDINATE);
		object.setPickupType(StopTime.PickupType.PHONE);

		for (int n = 0; n < 5; n++) {
			object.addArrivalTime(n);
		}

		String json = mapper.writeValueAsString(object);
		StopTime st = mapper.readValue(json, StopTime.class);

		assertEquals(object, st );

	}

	/**
	 * 
	 */
	public static StopTime createStopTime() {
		StopTime st = new StopTime();

		st.setStopHeadSign("Head Sign");
		st.setStopName("name");
		st.setStopId("537");
		st.setTripId("523");
		st.setDropOffType(StopTime.PickupType.COORDINATE);
		st.setPickupType(StopTime.PickupType.PHONE);

		for (int n = 0; n < 5; n++) {
			st.addArrivalTime(n);
		}

		return st;
	}

	/**
	 * 
	 */
	@Test
	public void testSetGets() {
		StopTime st = StopTimeTest.createStopTime();

		assertNotNull(st.getArrivalTime());
		assertEquals(5, st.getArrivalTime().size());
		for (int n = 0; n < st.getArrivalTime().size(); n++) {
			assertEquals(n, st.getArrivalTime().get(n).intValue());
		}

		assertEquals(StopTime.PickupType.PHONE, st.getPickupType());
		assertEquals(StopTime.PickupType.COORDINATE, st.getDropOffType());
		assertEquals("Head Sign", st.getStopHeadSign());
		assertEquals("name", st.getStopName());
		assertEquals("537", st.getStopId());
		assertEquals("523", st.getTripId());

	}

	@Test
	public void testCSV() {
		StopTime lhs = StopTimeTest.createStopTime();

		String csv = lhs.toCSVLine();
		StopTime rhs = new StopTime();
		rhs.fromCSVLine(csv);

		assertEquals(lhs.getPickupType(), rhs.getPickupType());
		assertEquals(lhs.getDropOffType(), rhs.getDropOffType());
		assertEquals(lhs.getStopHeadSign(), rhs.getStopHeadSign());
		assertEquals(lhs.getStopId(), rhs.getStopId());
		assertEquals(lhs.getTripId(), rhs.getTripId());

		assertNotNull(rhs.getArrivalTime());
		assertEquals(lhs.getArrivalTime().size(), rhs.getArrivalTime().size());
		for (int n = 0; n < rhs.getArrivalTime().size(); n++) {
			assertEquals(lhs.getArrivalTime().get(0).intValue(), rhs.getArrivalTime().get(0).intValue());
		}

	}

}
