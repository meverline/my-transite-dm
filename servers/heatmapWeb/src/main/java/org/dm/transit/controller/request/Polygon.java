package org.dm.transit.controller.request;

import me.math.Vertex;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

public class Polygon implements GeoShape {

    private List<Vertex> vertices;
    private boolean closed;

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public Geometry shape() {
        return null;
    }

}
