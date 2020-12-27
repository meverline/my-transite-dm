package me.transit.omd.dao.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import me.transit.omd.data.Location;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class GeometryFixer extends StdConverter<Location,Location> {

    @Override
    public Location convert(Location value) {
        GeometryFactory factory = new GeometryFactory();
        Point loc =  factory.createPoint(new Coordinate(value.getLat(), value.getLon()));
        value.setLocation(loc);
        return value;
    }
}
