package me.datamining.shapes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.locationtech.jts.geom.Geometry;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
@JsonSubTypes({ @JsonSubTypes.Type(value = Circle.class, name = "Circle"),
				@JsonSubTypes.Type(value = Polygon.class, name = "Polygon"),
				@JsonSubTypes.Type(value = Rectanlge.class, name = "Rectanlge") })
public interface Shape {

	Geometry shape();
}
