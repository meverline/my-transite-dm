package me.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.database.neo4j.GraphDatabaseDAO;
import me.database.neo4j.IGraphDatabaseDAO;

/**
 * 
 * @author markeverline
 *
 */
@Configuration
public class AppConfig {

	/**
	 * The Graph Datbase
	 * @return IGraphDatabaseDAO
	 */
	@Bean
	public IGraphDatabaseDAO graphDatabase() {
		return GraphDatabaseDAO.instance();
	}
	
}