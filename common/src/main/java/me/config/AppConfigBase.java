package me.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import me.database.hibernate.HibernateConnection;
import me.database.hibernate.SpringHibernateConnection;

public abstract class AppConfigBase {
	
	/**
	 * 
	 * @return
	 */
	protected abstract String[] packageToScan();

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
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.springframework.jdbc.datasource.DriverManagerDataSource");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/Transit");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres");

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
		hibernateProperties.setProperty("hibernate.show_sql", "false");
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");

		return hibernateProperties;
	}

}
