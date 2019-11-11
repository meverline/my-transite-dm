package me.transit.parser.data.converters;

import java.util.HashMap;
import java.util.Map;

public class DataConverterFactory {

	private final Map<Class<?>, DataConverter> handlers = new HashMap<>();
	private static DataConverterFactory self = null;

	public DataConverterFactory()
	{
		register( new IdToServiceDate());
		register( new LatlonToPoint());
		register( new StringConverter());
		register( new StringToBoolean());
		register( new StringToCalendar());
		register( new StringToEnum());
		register( new StringToGeometry());
		register( new StringToInt());
		register( new StringToDouble());
		register( new StringToLong());
		register( new StringToObject());
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static DataConverterFactory create() {
		if ( self == null ) {
			self = new DataConverterFactory();
		}
		return self;
	}
	
	/**
	 * 
	 * @param converter
	 */
	protected void register(DataConverter converter) {
		if ( converter != null && getHandler(converter.getType()) == null ) {
			handlers.put(converter.getType(), converter);
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public DataConverter getHandler(Class<?> type) {
		
		if ( handlers.containsKey(type)) {
			return (DataConverter) handlers.get(type).clone();
		}
		return null;
	}
		
}
