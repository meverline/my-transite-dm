package me.database;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import me.transit.database.RouteGeometry;

public class ShapeConverter implements Converter {

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
    	RouteGeometry shape = (RouteGeometry) source;
        writer.setValue(String.valueOf(shape.getUUID()));
    }

    /*
     * (non-Javadoc)
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
    	RouteGeometry calendar = new RouteGeometry();
        calendar.setUUID(Long.parseLong(reader.getValue()));
        return calendar;
    }

    /*
     * (non-Javadoc)
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return type.equals(RouteGeometry.class);
    }
}
