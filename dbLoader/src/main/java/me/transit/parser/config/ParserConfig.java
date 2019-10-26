package me.transit.parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.parser.AbstractGTFSParser;
import me.transit.parser.LocalParser;
import me.transit.parser.ParserService;
import me.transit.parser.data.FileHandlerFactory;

@Configuration
public class ParserConfig {

	@Bean
	public AbstractGTFSParser parserService(FileHandlerFactory fileHandlerFactory) {
		return new ParserService(fileHandlerFactory);
	}
	
	@Bean
	public AbstractGTFSParser localParser(FileHandlerFactory fileHandlerFactory, IGraphDatabaseDAO graphDatabase) {
		return new LocalParser(fileHandlerFactory, graphDatabase);
	}
}
