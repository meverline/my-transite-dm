package me.transit.database.converters;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import me.transit.database.RouteTrip;

@Converter
public class RouteTripToString implements AttributeConverter<List<RouteTrip>, String> {

	@Override
	public String convertToDatabaseColumn(List<RouteTrip> attribute) {
		StringBuffer data = new StringBuffer();
		for (RouteTrip item : attribute) {
			data.append(item.toString());
			data.append("\n");
		}
		return data.toString();
	}

	@Override
	public List<RouteTrip> convertToEntityAttribute(String dbData) {
		String lines[] = dbData.split("\n");
		List<RouteTrip> rtn = new ArrayList<>();
		for (String line : lines) {
			rtn.add(new RouteTrip(line));
		}
		return rtn;
	}

}
