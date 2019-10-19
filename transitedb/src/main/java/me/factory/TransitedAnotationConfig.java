package me.factory;

import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import me.transit.database.Agency;
import me.transit.database.CalendarDate;
import me.transit.database.Route;
import me.transit.database.RouteGeometry;
import me.transit.database.ServiceDate;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

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
	
}
