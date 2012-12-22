package me.database;

import com.vividsolutions.jts.geom.Point;

public class PointConverter extends GeomentryValueConverter {

    /*
     * (non-Javadoc)
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return type.equals(Point.class);
    }
}
