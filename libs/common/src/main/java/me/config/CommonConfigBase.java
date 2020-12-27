package me.config;

import me.database.nsstore.DocumentSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages="me.transit")
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
@EnableTransactionManagement
public class CommonConfigBase {
	
    @Autowired
    private Environment env;

	private Log log = LogFactory.getLog(CommonConfigBase.class);
    
	/**
	 * 
	 * @return
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
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
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
	private Properties hibernateProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		return hibernateProperties;
	}

}
