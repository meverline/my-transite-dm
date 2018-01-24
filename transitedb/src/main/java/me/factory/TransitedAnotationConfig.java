package me.factory;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import me.transit.dao.AgencyDao;
import me.transit.dao.CalendarDateDao;
import me.transit.dao.RouteDao;
import me.transit.dao.RouteGeometryDao;
import me.transit.dao.impl.AgencyDaoImpl;
import me.transit.dao.impl.CalendarDateDaoImpl;
import me.transit.dao.impl.RouteDaoImpl;
import me.transit.dao.impl.RouteGeometryDaoImpl;
import me.transit.database.Agency;
import me.transit.database.CalendarDate;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.TransitStop;
import me.transit.database.Trip;
import me.transit.database.Route;

@Configuration
public class TransitedAnotationConfig extends AnotationConfig {
		
	/*
	 * (non-Javadoc)
	 * @see me.factory.AnotationConfig#sessionBuilder()
	 */
	@Override
	protected LocalSessionFactoryBuilder sessionBuilder() {
		LocalSessionFactoryBuilder local = super.sessionBuilder();
	
		local.addAnnotatedClasses(Agency.class);
		local.addAnnotatedClasses(CalendarDate.class);
		local.addAnnotatedClasses(Route.class);
		local.addAnnotatedClasses(RouteGeometry.class);
		local.addAnnotatedClasses(ServiceDate.class);
		local.addAnnotatedClasses(TransitStop.class);
		local.addAnnotatedClasses(Trip.class);
	    
		return local;
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public AgencyDao agencyDao() throws ClassNotFoundException, SQLException {
		return new AgencyDaoImpl(connection());
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public CalendarDateDao calendarDateDao() throws ClassNotFoundException, SQLException {
		return new CalendarDateDaoImpl(connection());
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public RouteDao routeDao() throws ClassNotFoundException, SQLException {
		return new RouteDaoImpl(connection());
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public RouteGeometryDao routeGeometryDao() throws ClassNotFoundException, SQLException {
		return new RouteGeometryDaoImpl(connection());
	}
		
}
