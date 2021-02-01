package me.transit.parser.config;

import me.transit.parser.FeedParserListener;
import me.transit.parser.data.Blackboard;
import me.transit.omd.dao.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import(TransitDatabaseConfig.class)
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class ParserConfig {

	@Autowired
	private Environment env;

	@Bean(value="parserService")
	public AbstractGTFSParser parserService(FileHandlerFactory fileHandlerFactory,
											LocationDao locationDao,
											Blackboard blackboard,
											IGraphDatabaseDAO graphDatabase) {
		return new ParserService(fileHandlerFactory, locationDao, blackboard,
								 graphDatabase,
								 env.getProperty("aws.sqs.loader.uri"));
	}

	@Bean
	public FeedParserListener sqsLifecycle(FileHandlerFactory fileHandlerFactory,
										   LocationDao locationDao,
										   Blackboard blackboard,
										   IGraphDatabaseDAO graphDatabase) {
		Boolean isService = env.getProperty("dbLoader.isService", Boolean.class);
		return new FeedParserListener(this.parserService(fileHandlerFactory,
				locationDao, blackboard, graphDatabase), isService);
	}
	
	@Bean(value="localParser")
	public AbstractGTFSParser localParser(FileHandlerFactory fileHandlerFactory,
										  IGraphDatabaseDAO graphDatabase,
										  LocationDao locationDao, Blackboard blackboard) {
		return new LocalParser(fileHandlerFactory, graphDatabase, locationDao, blackboard);
	}
	
	@Bean(value="dataConverterFactory")
	public DataConverterFactory dataConverterFactory() {
		return DataConverterFactory.create();
	}
}
