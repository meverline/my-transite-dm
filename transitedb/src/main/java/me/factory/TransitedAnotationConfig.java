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
import me.transit.database.impl.AgencyImpl;
import me.transit.database.impl.CalendarDateImpl;
import me.transit.database.impl.RouteGeometryImpl;
import me.transit.database.impl.RouteImpl;
import me.transit.database.impl.ServiceDateImpl;
import me.transit.database.impl.TransitStopImpl;
import me.transit.database.impl.TripImpl;

@Configuration
public class TransitedAnotationConfig extends AnotationConfig {
		
	/*
	 * (non-Javadoc)
	 * @see me.factory.AnotationConfig#sessionBuilder()
	 */
	@Override
	protected LocalSessionFactoryBuilder sessionBuilder() {
		LocalSessionFactoryBuilder local = super.sessionBuilder();
	
		local.addAnnotatedClasses(AgencyImpl.class);
		local.addAnnotatedClasses(CalendarDateImpl.class);
		local.addAnnotatedClasses(RouteImpl.class);
		local.addAnnotatedClasses(RouteGeometryImpl.class);
		local.addAnnotatedClasses(ServiceDateImpl.class);
		local.addAnnotatedClasses(TransitStopImpl.class);
		local.addAnnotatedClasses(TripImpl.class);
	    
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
