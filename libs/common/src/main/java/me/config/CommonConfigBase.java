package me.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages="me.transit")
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
@EnableTransactionManagement
public class CommonConfigBase {
	
    @Autowired
    private Environment env;
	private final Log log = LogFactory.getLog(CommonConfigBase.class);

	protected Environment getEnv() {
		return env;
	}

	protected Log getLog() {
		return log;
	}

	/**
	 * 
	 * @return String[]
	 */
	protected String[] packageToScan() {
		String [] data = {
				"me.transit.database",
				"me.crime.database",
				"me.math.grid.tiled",
				"me.transit.omd.data"
		};
		
		return data;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcePlaceHolderConfiguer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 *
	 * @return LocalSessionFactoryBean
	 */
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(this.packageToScan());
		sessionFactory.setHibernateProperties(this.hibernateProperties());
		return sessionFactory;
	}

	/**
	 * 
	 * @return DataSource
	 */
	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(getEnv().getProperty("database.jdbc.connection"));
		dataSource.setUsername(getEnv().getProperty("database.username"));
		dataSource.setPassword(getEnv().getProperty("database.password"));

		return dataSource;
	}

	/**
	 * 
	 * @return Properties
	 */
	private Properties hibernateProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.show_sql", getEnv().getProperty("hibernate.show_sql"));
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", getEnv().getProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.setProperty("hibernate.dialect", getEnv().getProperty("hibernate.dialect"));
		return hibernateProperties;
	}

	@Bean
	public HibernateTransactionManager transactionManager(LocalSessionFactoryBean sessionFactoryBean) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactoryBean.getObject());
		return transactionManager;
	}

}
