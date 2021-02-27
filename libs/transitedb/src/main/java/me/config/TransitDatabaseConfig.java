package me.config;

import lombok.extern.apachecommons.CommonsLog;
import me.database.dynamo.DynamoDocumentSession;
import me.database.mongo.MongoDocumentSession;
import me.database.neo4j.GraphDatabaseDAO;
import me.database.neo4j.IGraphDatabaseDAO;
import me.database.nsstore.DocumentSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author markeverline
 *
 */
@Configuration
@Import(CommonConfigBase.class)
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
@CommonsLog
public class TransitDatabaseConfig {

	private enum DocumentSessionFactory {
		MongoDB {
			@Override
			public DocumentSession factory(Map<String, String> properties) {
				return new MongoDocumentSession(properties);
			}
		},
		DynomoDB {
			@Override
			public DocumentSession factory(Map<String, String> properties) {
				return new DynamoDocumentSession(properties);
			}
		};

		public abstract DocumentSession factory(Map<String, String> properties);
	}

    @Autowired
    private Environment env;

	private Map<String, String> documentProperties()
	{
		final Map<String, String> properties = new HashMap<>();

		properties.put(DocumentSession.HOST, env.getProperty("nosql.document.store.host"));
		properties.put(DocumentSession.PORT, env.getProperty("nosql.document.store.port"));
		properties.put(DocumentSession.DATABASE, env.getProperty("nosql.document.store.database"));

		return properties;
	}

	/**
	 * The Graph Datbase
	 * @return IGraphDatabaseDAO
	 */
	@Bean
	public IGraphDatabaseDAO graphDatabase() {
		return GraphDatabaseDAO.instance(env.getProperty("neo4j.databasepath"));
	}

	@Bean
	@Scope("singleton")
	public DocumentSession documentDatabase() throws IllegalAccessException {

		final String storeClass = env.getProperty("nosql.document.store.class");
		if ( storeClass == null ) {
			throw new IllegalAccessException("nosql.document.store.class is not specfied");
		}

		DocumentSession rtn;
		try {
			DocumentSessionFactory sessionFactory = DocumentSessionFactory.valueOf(storeClass);
			rtn = sessionFactory.factory(this.documentProperties());
		} catch (Exception e) {
			log.error(storeClass + " " + e.getLocalizedMessage(), e);
			rtn = new FakeDocumentSession();
		}

		return rtn;
	}

}