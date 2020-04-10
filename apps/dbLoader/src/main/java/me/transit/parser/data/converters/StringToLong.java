package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

@Service(value="stringToLong")
public class StringToLong extends DataConverter {
	
	@Override
	public Class<?> getType() {
		return Long.TYPE;
	}

	@Override
	public Object convert(String data) {
		Long rtn = null;
		if (data.contains(":")) {
			String time[] = data.split(":");
			String str = time[0] + time[1];
			rtn = new Long(str);
		} else {
			rtn = new Long(data);
		}
		return rtn;
	}

	@Override
	public DataConverter clone() {
		return new StringToLong();
	}
	
}
