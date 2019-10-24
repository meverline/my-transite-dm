package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

@Service(value="stringToObject")
public class StringToObject extends DataConverter {

	@Override
	public Class<?> getType() {
		return Object.class;
	}

	
	@Override
	public Object convert(String data) {
		return data;
	}
	
	@Override
	public DataConverter clone() {
		return new StringToObject();
	}

}
