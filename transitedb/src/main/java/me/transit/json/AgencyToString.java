package me.transit.json;

import com.fasterxml.jackson.databind.util.StdConverter;

import me.transit.database.Agency;

public class AgencyToString extends StdConverter<Agency, String> {

	@Override
	public String convert(Agency value) {
		return String.format("%d %s", value.getUUID(), value.getName() );
	}

}
