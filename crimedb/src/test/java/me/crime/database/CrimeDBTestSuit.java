package me.crime.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({me.crime.database.AddressTest.class,
					 CrimeTest.class,
					 me.crime.database.CrimeTest.class,
					 GeoPointTest.class })
public class CrimeDBTestSuit {

}
