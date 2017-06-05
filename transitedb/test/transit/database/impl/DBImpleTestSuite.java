package transit.database.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AgencyImpleTest.class,
	                 CalendarDateImplTest.class,
	                 RouteGeometryImplTest.class,
	                 transit.database.impl.RouteImplTest.class,
	                 transit.database.impl.RouteStopDataImplTest.class,
	                 transit.database.impl.ServiceDateImplTest.class,
	                 transit.database.impl.StopTimeImplTest.class,
	                 transit.database.impl.TransitStopImplTest.class,
	                 transit.database.impl.TripImplTest.class})
public class DBImpleTestSuite {

}
