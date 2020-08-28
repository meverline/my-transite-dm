package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

@Service(value="stringConverter")
public class StringConverter extends DataConverter {
	
	@Override
	public Class<?> getType() {
		return String.class;
	}

	@Override
	public Object convert(String data) {
		String rtn = data;
		if (rtn.contains("\"")) {
			rtn = rtn.replace('"', ' ').trim();
		}
		return rtn;
	}
	
	@Override
	public DataConverter clone() {
		return new StringConverter();
	}
	

}
