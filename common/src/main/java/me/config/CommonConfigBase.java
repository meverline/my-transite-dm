package me.config;

import java.net.UnknownHostException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import me.database.hibernate.HibernateConnection;
import me.database.hibernate.SpringHibernateConnection;
import me.database.mongo.DocumentDao;
import me.database.mongo.IDocumentDao;


@Configuration
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
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
				"me.crime.database"
		};
		
		return data;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(this.packageToScan());
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
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
	@Bean
	public HibernateConnection hibernateConnection() {
		return new SpringHibernateConnection(this.sessionFactory().getObject());
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
