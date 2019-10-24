package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

@Service(value="stringToDouble")
public class StringToDouble extends DataConverter {

	@Override
	public Class<?> getType() {
		return Double.TYPE;
	}
	
	@Override
	public Object convert(String data) {
		return new Double(data);
	}
	
	@Override
	public DataConverter clone() {
		return new StringToDouble();
	}

}
