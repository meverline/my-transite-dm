package me.transit.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({me.transit.dao.mongo.MongoDBTest.class,
					 me.transit.dao.neo4j.GraphDatabaseDAOTest.class
	                })
public class DaoSuite {

}
