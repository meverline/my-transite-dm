package me.transit.parser.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="fileHandlerFactory")
public class FileHandlerFactory {
	
	private final Map<String, FileHandler> handlers = new HashMap<>();
	private final DefaultFileHandler defaultHandler;

	@Autowired
	public FileHandlerFactory(DefaultFileHandler defaultHandler, ShapeFileHandler shapeFileHandler, 
							  StopTimeFileHandler stopTimeFileHandler, TripFileHandler tripFileHandler,
							  ServiceDateFileHandler serviceDateFileHandler)
	{
		
		this.defaultHandler = defaultHandler;
		handlers.put(shapeFileHandler.handlesFile(), shapeFileHandler);
		handlers.put(serviceDateFileHandler.handlesFile(), serviceDateFileHandler);
		handlers.put(stopTimeFileHandler.handlesFile(), stopTimeFileHandler);
		handlers.put(tripFileHandler.handlesFile(), tripFileHandler);
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public FileHandler getHandler(String fileName) {
		
		if ( handlers.containsKey(fileName)) {
			return handlers.get(fileName);
		}
		return this.defaultHandler;
	}
	
	public void reset() {
		this.defaultHandler.getBlackboard().reset();
	}
	
	
}
