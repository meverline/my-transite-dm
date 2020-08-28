package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

@Service(value="stringToBoolean")
public class StringToBoolean extends DataConverter {
	
	@Override
	public Class<?> getType() {
		return Boolean.TYPE;
	}
	
	@Override
	public Object convert(String data) {
		return new Boolean(data);
	}
	
	@Override
	public DataConverter clone() {
		return new StringToBoolean();
	}
	

}
