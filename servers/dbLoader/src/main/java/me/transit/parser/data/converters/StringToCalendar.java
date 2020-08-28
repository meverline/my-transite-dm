package me.transit.parser.data.converters;

import java.util.Calendar;

import org.springframework.stereotype.Service;

@Service(value="stringToCalendar")
public class StringToCalendar extends DataConverter {
	
	@Override
	public Class<?> getType() {
		return Calendar.class;
	}

	@Override
	public Object convert(String data) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.YEAR, Integer.parseInt(data.substring(0, 4).replace('"', ' ').trim()));
		cal.set(Calendar.MONTH, Integer.parseInt(data.substring(4, 6).replace('"', ' ').trim()) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data.substring(6).replace('"', ' ').trim()));
		return cal;
	}
	
	@Override
	public DataConverter clone() {
		return new StringToCalendar();
	}

}
