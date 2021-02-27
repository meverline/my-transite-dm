package me.transit.dao.query.tuple;

import lombok.extern.apachecommons.CommonsLog;
import me.output.KmlFormatter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import java.util.ArrayList;
import java.util.List;

@CommonsLog
public class CircleTuple extends AbstractQueryTuple {

    private final GeometryFactory factory_  = new GeometryFactory();
    private final double distanceInMeters_;
    private final Point center_;

    /**
     *
     * @param field
     * @param center
     * @param distanceInMeters
     */
    public CircleTuple(String field, Point center, double distanceInMeters )
    {
        super(null, field);
        center_ = center;
        distanceInMeters_ = distanceInMeters;
    }

    /**
     *
     * @param alias
     * @param field
     * @param center
     * @param distanceInMeters
     */
    public CircleTuple(Class<?> alias, String field, Point center, double distanceInMeters )
    {
        super(alias, field);
        center_ = center;
        distanceInMeters_ = distanceInMeters;
    }

    /**
     *
     */
    public boolean hasMultipleCriterion()
    {
        return true;
    }

    /**
     * @return The Radius of the earth at the equator.
     */
    protected static double equatorialRadiusInMeters()
    {
        return 6378137.0;
    }

    public GeometryFactory getFactory() {
        return factory_;
    }

    public double getDistanceInMeters() {
        return distanceInMeters_;
    }

    public Point getCenter() {
        return center_;
    }

    /**
     *
     * @param location
     * @param radiusMeters
     * @return
     */
    public Polygon makeCircle(Point location, double radiusMeters) {

        double distance = radiusMeters / CircleTuple.equatorialRadiusInMeters() * 2;
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(32);
        shapeFactory.setCentre(new Coordinate(location.getX(), location.getY()));
        shapeFactory.setSize(radiusMeters / CircleTuple.equatorialRadiusInMeters() * 2);
        Polygon circle = shapeFactory.createCircle();

        List<Coordinate> coords = new ArrayList<>();
        for ( Coordinate coord : circle.getCoordinates()) {
            coords.add( coord );
        }

        coords.add(coords.get(0));
        Coordinate [] array = new Coordinate[coords.size()];
        coords.toArray(array);

        StringBuilder kml = new StringBuilder();
        KmlFormatter.start(kml);
        KmlFormatter.format(kml, location);
        KmlFormatter.formatPointsOnly(kml, array, "");
        KmlFormatter.end(kml);
        CircleTuple.log.info(kml.toString());
        CircleTuple.log.info( "Distance:" + radiusMeters + " " + distance);

        return factory_.createPolygon(factory_.createLinearRing(array), null);
    }

}
