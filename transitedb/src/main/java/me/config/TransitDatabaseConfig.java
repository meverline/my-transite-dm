package me.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import me.database.neo4j.GraphDatabaseDAO;
import me.database.neo4j.IGraphDatabaseDAO;

/**
 * 
 * @author markeverline
 *
 */
@Configuration
@Import(CommonConfigBase.class)
public class TransitDatabaseConfig {

	/**
	 * The Graph Datbase
	 * @return IGraphDatabaseDAO
	 */
	@Bean
	public IGraphDatabaseDAO graphDatabase() {
		return GraphDatabaseDAO.instance();
	}
	
}