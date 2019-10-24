package me.transit.parser.data.converters;

import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

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
