package me.transit.parser;

import java.net.UnknownHostException;
import java.util.List;

import me.factory.DaoBeanFactory;
import me.transit.dao.AgencyDao;
import me.transit.dao.RouteDao;
import me.transit.dao.impl.RouteDaoImpl.RouteListIterator;
import me.transit.dao.mongo.DocumentDao;
import me.transit.database.Agency;
import me.transit.database.Route;

public class DumpRouteTrips {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DaoBeanFactory.initilize();
		
		AgencyDao agencyDao = AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean(AgencyDao.class));
		RouteDao routeDao = RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));
		
		long count = 0;
		try {
					
			DocumentDao docDao = DocumentDao.instance();
			
			List<Agency> list = agencyDao.list();
			for ( Agency agency : list) {
				docDao.add(agency);
			}
			
			RouteListIterator iterator = routeDao.listAllRoutes();
			
			boolean done = false;
			while ( iterator.hasNext() && (! done) ) {
				Route rt = iterator.next();
				if ( rt != null ) {
					docDao.add(rt);
					count++;
					if ( count % 100 == 0 ) {
						System.out.println("inserted " + count);
					}
				} else {
					done = true;
				}

			}
			iterator.done();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("done fetched: " + count);
		
	}

}
