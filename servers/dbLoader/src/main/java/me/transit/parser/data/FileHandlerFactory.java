package me.transit.parser.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="fileHandlerFactory")
public class FileHandlerFactory {
	
	private Log log = LogFactory.getLog(getClass().getName());
	private final Map<String, AbstractFileHandler> handlers = new HashMap<>();

	@Autowired
	public FileHandlerFactory(ShapeFileHandler shapeFileHandler, 
							  StopTimeFileHandler stopTimeFileHandler, 
							  TripFileHandler tripFileHandler,
							  ServiceDateFileHandler serviceDateFileHandler,
							  AgencyDataFileHandler agencyDataFileHandler,
							  CalendarDateFileHandler calendarDateFileHandler,
							  RouteFileHandler routeFileHandler,
							  TransitStopFileHandler transitStopFileHandler)
	{
		Objects.requireNonNull(shapeFileHandler, "shapeFileHandler is required");
		Objects.requireNonNull(serviceDateFileHandler, "serviceDateFileHandler is required");
		Objects.requireNonNull(stopTimeFileHandler, "stopTimeFileHandler is required");
		Objects.requireNonNull(tripFileHandler, "tripFileHandler is required");
		Objects.requireNonNull(agencyDataFileHandler, "agencyDataFileHandler is required");
		Objects.requireNonNull(calendarDateFileHandler, "calendarDateFileHandler is required");
		Objects.requireNonNull(routeFileHandler, "routeFileHandler is required");
		Objects.requireNonNull(transitStopFileHandler, "transitStopFileHandler is required");
		
		handlers.put(shapeFileHandler.handlesFile(), shapeFileHandler);
		handlers.put(serviceDateFileHandler.handlesFile(), serviceDateFileHandler);
		handlers.put(stopTimeFileHandler.handlesFile(), stopTimeFileHandler);
		handlers.put(agencyDataFileHandler.handlesFile(), agencyDataFileHandler);
		handlers.put(calendarDateFileHandler.handlesFile(), calendarDateFileHandler);
		handlers.put(routeFileHandler.handlesFile(), routeFileHandler);
		handlers.put(transitStopFileHandler.handlesFile(), transitStopFileHandler);
		handlers.put(tripFileHandler.handlesFile(), tripFileHandler);
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public AbstractFileHandler getHandler(String fileName) throws Exception {
		
		if ( handlers.containsKey(fileName)) {
			return handlers.get(fileName);
		}
		log.error("unknonwn file type: " + fileName);
		throw new Exception("unknonwn file type: " + fileName);
	}
	
}
