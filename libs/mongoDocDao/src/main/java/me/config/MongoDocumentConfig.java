package me.config;

import me.database.mongo.DocumentDao;
import me.database.mongo.IDocumentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;

@Configuration
@Import(CommonConfigBase.class)
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
public class MongoDocumentConfig {

    @Autowired
    private Environment env;

    @Bean
    public IDocumentDao documentDatabase() throws UnknownHostException {
        return DocumentDao.instance();
    }

}