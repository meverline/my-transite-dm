package me.transit.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({me.transit.dao.neo4j.GraphTestSuite.class})
public class DaoSuite {

}
