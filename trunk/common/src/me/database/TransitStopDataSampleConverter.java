package me.database;

import me.datamining.sample.AbstractSpatialSampleData;
import me.datamining.sample.TransitStopSpatialSample;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class TransitStopDataSampleConverter implements Converter {

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
    	AbstractSpatialSampleData shape = (AbstractSpatialSampleData) source;
        writer.setValue(String.valueOf(shape.getValue()));
    }

    /*
     * (non-Javadoc)
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return type.equals(TransitStopSpatialSample.class);
    }
}