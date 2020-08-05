package me.datamining.shapes;

import org.locationtech.jts.geom.Geometry;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
public interface Shape {

	Geometry shape();
}
