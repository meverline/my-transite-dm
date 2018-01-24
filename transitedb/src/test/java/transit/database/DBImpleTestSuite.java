package transit.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	                 RouteTest.class,
	                 RouteStopDataImplTest.class,
	                 ServiceDateTest.class,
	                 StopTimeTest.class,
	                 TransitStopTest.class,
	                 TripTest.class})
public class DBImpleTestSuite {

}
