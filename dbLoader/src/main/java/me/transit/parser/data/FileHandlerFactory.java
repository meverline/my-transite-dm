package me.transit.parser.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="fileHandlerFactory")
public class FileHandlerFactory {
	
	private final Map<String, AbstractFileHandler> handlers = new HashMap<>();
	private final DefaultFileHandler defaultHandler;

	@Autowired
	public FileHandlerFactory(DefaultFileHandler defaultHandler, ShapeFileHandler shapeFileHandler, 
							  StopTimeFileHandler stopTimeFileHandler, TripFileHandler tripFileHandler,
							  ServiceDateFileHandler serviceDateFileHandler)
	{
		Objects.requireNonNull(shapeFileHandler, "shapeFileHandler is required");
		Objects.requireNonNull(serviceDateFileHandler, "serviceDateFileHandler is required");
		Objects.requireNonNull(stopTimeFileHandler, "stopTimeFileHandler is required");
		Objects.requireNonNull(tripFileHandler, "tripFileHandler is required");
		
		this.defaultHandler = Objects.requireNonNull(defaultHandler, "defaultHandler is required");
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
	public AbstractFileHandler getHandler(String fileName) {
		
		if ( handlers.containsKey(fileName)) {
			return handlers.get(fileName);
		}
		return this.defaultHandler;
	}
	
	public void reset() {
		this.defaultHandler.getBlackboard().reset();
	}
	
	
}
