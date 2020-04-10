package transit.database;

import static org.junit.Assert.*;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.postgresql.Driver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

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
				"postgres");
	}

	@Test
	public void test() {

		try {
			LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource());

			sessionBuilder.addProperties(getHibernateProperties());

			System.out.println("start registering class");
			sessionBuilder.addAnnotatedClasses(Agency.class);
			System.out.println("done: " + Agency.class.getName());
			sessionBuilder.addAnnotatedClasses(CalendarDate.class);
			System.out.println("done: " + CalendarDate.class.getName());
			sessionBuilder.addAnnotatedClasses(Route.class);
			System.out.println("done: " + Route.class.getName());
			sessionBuilder.addAnnotatedClasses(RouteGeometry.class);
			System.out.println("done: " + RouteGeometry.class.getName());
			sessionBuilder.addAnnotatedClasses(ServiceDate.class);
			System.out.println("done: " + ServiceDate.class.getName());
			sessionBuilder.addAnnotatedClasses(TransitStop.class);
			System.out.println("done: " + TransitStop.class.getName());
			sessionBuilder.addAnnotatedClasses(Trip.class);
			System.out.println("done: " + Trip.class.getName());

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
