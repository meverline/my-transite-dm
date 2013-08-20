package me.transit.database.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({me.transit.database.impl.AgencyImpleTest.class,
	                 me.transit.database.impl.CalendarDateImplTest.class,
	                 me.transit.database.impl.RouteGeometryImplTest.class,
	                 me.transit.database.impl.RouteImplTest.class,
	                 me.transit.database.impl.RouteStopDataImplTest.class,
	                 me.transit.database.impl.ServiceDateImplTest.class,
	                 me.transit.database.impl.StopTimeImplTest.class,
	                 me.transit.database.impl.TransitStopImplTest.class,
	                 me.transit.database.impl.TripImplTest.class})
public class DBImpleTestSuite {

}
