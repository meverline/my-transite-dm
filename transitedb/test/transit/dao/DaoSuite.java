package transit.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({transit.dao.mongo.MongoDBTest.class,
					 transit.dao.neo4j.GraphDatabaseDAOTest.class
	                })
public class DaoSuite {

}
