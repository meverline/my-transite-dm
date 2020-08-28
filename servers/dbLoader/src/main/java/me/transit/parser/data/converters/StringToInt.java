package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

@Service(value="stringToInt")
public class StringToInt extends DataConverter {

	@Override
	public Class<?> getType() {
		return Integer.TYPE;
	}

	@Override
	public Object convert(String data) {
		return new Integer(data);
	}

	@Override
	public DataConverter clone() {
		return new StringToInt();
	}

}
