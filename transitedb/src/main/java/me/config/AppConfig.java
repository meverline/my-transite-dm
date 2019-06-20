package me.config;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.transit.dao.AgencyDao;
import me.transit.dao.CalendarDateDao;
import me.transit.dao.RouteDao;
import me.transit.dao.RouteGeometryDao;
import me.transit.dao.ServiceDateDao;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.TripDao;
import me.transit.dao.impl.AgencyDaoImpl;
import me.transit.dao.impl.CalendarDateDaoImpl;
import me.transit.dao.impl.RouteDaoImpl;
import me.transit.dao.impl.RouteGeometryDaoImpl;
import me.transit.dao.impl.ServiceDateDaoImpl;
import me.transit.dao.impl.TransiteStopDaoImpl;
import me.transit.dao.impl.TripDaoImpl;

/**
 * 
 * @author markeverline
 *
 */
@Configuration
public class AppConfig extends AppConfigBase {

	/*
	 * (non-Javadoc)
	 * @see me.config.AppConfigBase#packageToScan()
	 */
	@Override
	protected String[] packageToScan() {
		String pscan[] = { "me.transit.database" };
		return pscan;
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public AgencyDao agencyDao() throws ClassNotFoundException, SQLException {
		return new AgencyDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public CalendarDateDao calendarDateDao() throws ClassNotFoundException, SQLException {
		return new CalendarDateDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public RouteDao routeDao() throws ClassNotFoundException, SQLException {
		return new RouteDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public ServiceDateDao serviceDateDao() throws ClassNotFoundException, SQLException {
		return new ServiceDateDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public RouteGeometryDao routeGeometryDao() throws ClassNotFoundException, SQLException {
		return new RouteGeometryDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public TransiteStopDao transiteStopDao() throws ClassNotFoundException, SQLException {
		return new TransiteStopDaoImpl(this.hibernateConnection());
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public TripDao tripDao() throws ClassNotFoundException, SQLException {
		return new TripDaoImpl(this.hibernateConnection());
	}


}