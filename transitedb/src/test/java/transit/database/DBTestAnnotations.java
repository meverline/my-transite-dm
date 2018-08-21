package transit.database;

import static org.junit.Assert.*;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.postgresql.Driver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import me.transit.database.Agency;
import me.transit.database.CalendarDate;
import me.transit.database.Route;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

public class DBTestAnnotations {

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
				"Postgres");
	}

	@Test
	public void test() {

		try {
			LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource());

			sessionBuilder.addProperties(getHibernateProperties());

			sessionBuilder.addAnnotatedClasses(Agency.class);
			sessionBuilder.addAnnotatedClasses(CalendarDate.class);
			sessionBuilder.addAnnotatedClasses(Route.class);
			sessionBuilder.addAnnotatedClasses(RouteGeometry.class);
			sessionBuilder.addAnnotatedClasses(ServiceDate.class);
			sessionBuilder.addAnnotatedClasses(TransitStop.class);
			sessionBuilder.addAnnotatedClasses(Trip.class);

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
