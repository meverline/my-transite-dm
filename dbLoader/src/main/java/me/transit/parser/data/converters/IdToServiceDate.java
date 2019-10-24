package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

import me.transit.database.ServiceDate;

@Service(value="idToServiceDate")
public class IdToServiceDate extends DataConverter implements Cloneable {
		
	
	@Override
	public Class<?> getType() {
		return ServiceDate.class;
	}

	@Override
	public Object convert(String data) {
		long id = Long.parseLong(data.trim());
		return this.getBlackboard().getServiceDate(id);
	}
	
	@Override
	public DataConverter clone() {
		return new IdToServiceDate();
	}
	
}
