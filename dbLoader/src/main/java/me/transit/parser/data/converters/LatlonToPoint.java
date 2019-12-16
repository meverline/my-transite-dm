package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Service(value="latlonToPoint")
public class LatlonToPoint extends DataConverter {
	
	@Override
	public Class<?> getType() {
		return Point.class;
	}

	@Override
	public Object convert(String data) {
		GeometryFactory factory = new GeometryFactory();
		String world[] = data.split(",");
		double lat = Double.parseDouble(world[0]);
		double lon = Double.parseDouble(world[1]);
		return factory.createPoint(new Coordinate(lat, lon));
	}
	
	@Override
	public DataConverter clone() {
		return new LatlonToPoint();
	}
	

}
