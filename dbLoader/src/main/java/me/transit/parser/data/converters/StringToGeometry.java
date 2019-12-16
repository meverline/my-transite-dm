package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

import org.locationtech.jts.geom.Geometry;

@Service(value="stringToGeometry")
public class StringToGeometry extends DataConverter {
		
	@Override
	public Class<?> getType() {
		return Geometry.class;
	}

	@Override
	public Object convert(String data) {
		long id = Long.parseLong(data.trim());
		return this.getBlackboard().getShape(id);
	}
	
	@Override
	public DataConverter clone() {
		return new StringToGeometry();
	}


}
