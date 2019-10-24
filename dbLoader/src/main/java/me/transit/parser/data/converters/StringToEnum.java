package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

@Service(value="stringToEnum")
public class StringToEnum extends DataConverter {
		
	@Override
	public Class<?> getType() {
		return Enum.class;
	}

	@Override
	public Object convert(String data) {
		String rtn = data.trim();
		Object obj = null;
		if (rtn.contains("\"")) {
			rtn = rtn.replace('"', ' ').trim();
		}
		if (rtn.isEmpty() || rtn.length() < 1) {
			rtn = "UNKOWN";
			for (Object str : getReturnType().getEnumConstants()) {
				if (rtn.compareTo(str.toString()) == 0) {
					obj = str;
				}
			}
		} else {
			int index = Integer.parseInt(rtn);
			obj = getReturnType().getEnumConstants()[index];
		}
		return obj;
	}
	
	@Override
	public DataConverter clone() {
		return new StringToEnum();
	}

}
