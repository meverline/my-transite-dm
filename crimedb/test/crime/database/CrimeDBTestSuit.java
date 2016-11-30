package crime.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({crime.database.AddressTest.class,
					 crime.database.CrimeCatagoryTest.class,
					 crime.database.CrimeTest.class,
					 crime.database.GeoPointTest.class,
					 crime.database.URCCatagoriesTest.class})
public class CrimeDBTestSuit {

}
