package me.crime.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({me.crime.database.AddressTest.class,
					 me.crime.database.CrimeCatagoryTest.class,
					 me.crime.database.CrimeTest.class,
					 me.crime.database.GeoPointTest.class,
					 me.crime.database.URCCatagoriesTest.class})
public class CrimeDBTestSuit {

}
