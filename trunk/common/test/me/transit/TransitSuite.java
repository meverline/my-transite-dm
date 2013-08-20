package me.transit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({me.transit.database.DatabaseTestSuit.class,
					 me.transit.dao.DaoSuite.class})
public class TransitSuite {

}
