package org.dm.transit.controller.request;

import me.math.Vertex;
import org.locationtech.jts.geom.Geometry;

public class Rectangle implements GeoShape {

    private Vertex upperLeft;
    private Vertex lowerRight;

    public Vertex getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(Vertex upperLeft) {
        this.upperLeft = upperLeft;
    }

    public Vertex getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(Vertex lowerRight) {
        this.lowerRight = lowerRight;
    }

    @Override
    public Geometry shape() {
        return null;
    }

}
