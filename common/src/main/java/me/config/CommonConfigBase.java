package me.config;

import java.net.UnknownHostException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import me.database.mongo.DocumentDao;
import me.database.mongo.IDocumentDao;


@Configuration
@ComponentScan(basePackages="me.transit")
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
@EnableTransactionManagement
public class CommonConfigBase {
	
    @Autowired
    private Environment env;
    
	/**
	 * 
	 * @return
	 */
	protected String[] packageToScan() {
		String [] data = {
				"me.transit.database",
				"me.crime.database",
				"me.math.grid.tiled",
				"me.transit.parser.omd"
		};
		
		return data;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcePlaceHolderConfiguer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	@Scope("singleton")
	public SessionFactory sessionFactory() {
		return new LocalSessionFactoryBuilder(dataSource())
					.scanPackages(this.packageToScan())
					.addProperties(hibernateProperties())
					.buildSessionFactory();
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(env.getProperty("database.jdbc.connection"));
		dataSource.setUsername(env.getProperty("database.username"));
		dataSource.setPassword(env.getProperty("database.password"));

		return dataSource;
	}

	/**
	 * 
	 * @return
	 */
	private final Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		return hibernateProperties;
	}
	
	@Bean
	public IDocumentDao documentDatabase() throws UnknownHostException {
		return DocumentDao.instance();
	}

}
