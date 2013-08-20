package me;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({me.transit.TransitSuite.class,
	                 me.crime.CrimeDatabaseTestSuite.class})
public class RootSuit {

}
