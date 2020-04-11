package me.transit.parser.config;

import me.transit.parser.data.Blackboard;
import me.transit.omd.dao.LocationDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import me.config.TransitDatabaseConfig;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.parser.data.FileHandlerFactory;
import me.transit.parser.data.converters.DataConverterFactory;
import me.transit.parser.service.AbstractGTFSParser;
import me.transit.parser.service.LocalParser;
import me.transit.parser.service.ParserService;

@Configuration

@Import(TransitDatabaseConfig.class)
public class ParserConfig {

	@Bean(value="parserService")
	public AbstractGTFSParser parserService(FileHandlerFactory fileHandlerFactory, LocationDao locationDao, Blackboard blackboard) {
		return new ParserService(fileHandlerFactory, locationDao, blackboard);
	}
	
	@Bean(value="localParser")
	public AbstractGTFSParser localParser(FileHandlerFactory fileHandlerFactory, IGraphDatabaseDAO graphDatabase, LocationDao locationDao, Blackboard blackboard) {
		return new LocalParser(fileHandlerFactory, graphDatabase, locationDao, blackboard);
	}
	
	@Bean(value="dataConverterFactory")
	public DataConverterFactory dataConverterFactory() {
		return DataConverterFactory.create();
	}
}