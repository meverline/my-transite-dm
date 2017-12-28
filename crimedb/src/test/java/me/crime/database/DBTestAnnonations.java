package me.crime.database;

import static org.junit.Assert.*;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.postgresql.Driver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class DBTestAnnonations {

	/**
	 * 
	 * @return
	 */
	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");
		properties.put("hibernate.hbm2ddl.auto", "create");
		return properties;
	}

	/**
	 * 
	 * @return
	 */
	private SimpleDriverDataSource dataSource() {

		return new SimpleDriverDataSource(new Driver(), "jdbc:postgresql://localhost:5432/Transit", "postgres",
				"postgres");
	}

	@Test
	public void test() {

		try {
			LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource());

			sessionBuilder.addProperties(getHibernateProperties());

			sessionBuilder.addAnnotatedClasses(Address.class);
			sessionBuilder.addAnnotatedClasses(URCCatagories.class);
			sessionBuilder.addAnnotatedClasses(Crime.class);

			@SuppressWarnings("unused")
			SessionFactory sf = sessionBuilder.buildSessionFactory();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
		assertTrue(true);
		System.out.println("done");
	}

}
