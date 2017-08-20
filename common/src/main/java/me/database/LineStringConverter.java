package me.database;

import com.vividsolutions.jts.geom.LineString;

public class LineStringConverter extends GeomentryValueConverter {

    /*
     * (non-Javadoc)
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return type.equals(LineString.class);
    }
}
