package me.database;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public abstract class GeomentryValueConverter  implements Converter {

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
    	Geometry shape = (Geometry) source;
    	
    	StringBuilder builder = new StringBuilder();
    	
    	for ( Coordinate coord : shape.getCoordinates()) {
    		builder.append(coord.x);
    		builder.append(",");
    		builder.append(coord.y);
    		builder.append(",0\n");
    	}
    	
        writer.setValue(String.valueOf(builder.toString()));
    }

    /*
     * (non-Javadoc)
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        return null;
    }

}
