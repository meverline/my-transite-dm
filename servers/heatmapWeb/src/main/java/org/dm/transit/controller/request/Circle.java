package org.dm.transit.controller.request;

import me.math.Vertex;
import org.locationtech.jts.geom.Geometry;

public class Circle implements GeoShape {

    private double radiusInMeters;
    private Vertex center;

    public double getRadiusInMeters() {
        return radiusInMeters;
    }

    public void setRadiusInMeters(double radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }

    public Vertex getCenter() {
        return center;
    }

    public void setCenter(Vertex center) {
        this.center = center;
    }

    @Override
    public Geometry shape() {
        return null;
    }
}
