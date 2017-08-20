package transit.database.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	                 RouteImplTest.class,
	                 RouteStopDataImplTest.class,
	                 ServiceDateImplTest.class,
	                 StopTimeImplTest.class,
	                 TransitStopImplTest.class,
	                 TripImplTest.class})
public class DBImpleTestSuite {

}
