package me.transit.parser.config;

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
	public AbstractGTFSParser parserService(FileHandlerFactory fileHandlerFactory) {
		return new ParserService(fileHandlerFactory);
	}
	
	@Bean(value="localParser")
	public AbstractGTFSParser localParser(FileHandlerFactory fileHandlerFactory, IGraphDatabaseDAO graphDatabase) {
		return new LocalParser(fileHandlerFactory, graphDatabase);
	}
	
	@Bean(value="dataConverterFactory")
	public DataConverterFactory dataConverterFactory() {
		return DataConverterFactory.create();
	}
}
