package me.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import me.database.neo4j.GraphDatabaseDAO;
import me.database.neo4j.IGraphDatabaseDAO;

/**
 * 
 * @author markeverline
 *
 */
@Configuration
@Import(CommonConfigBase.class)
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
public class TransitDatabaseConfig {

    @Autowired
    private Environment env;

	/**
	 * The Graph Datbase
	 * @return IGraphDatabaseDAO
	 */
	@Bean
	public IGraphDatabaseDAO graphDatabase() {
		return GraphDatabaseDAO.instance(env.getProperty("neo4j.databasepath"));
	}
	
}