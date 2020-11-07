package me.transit.dao.query.translator.mongo;

import junit.framework.TestCase;
import me.transit.dao.query.tuple.CircleTuple;
import me.utils.TransiteEnums;
import org.bson.Document;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class MongoCircleTupleTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    private double distance = TransiteEnums.DistanceUnitType.MI.toMeters(1.1);
    private static final GeometryFactory factory_  = new GeometryFactory();
    private Point ul;

    @Before
    public void setUp() {
        ul = factory_.createPoint(new Coordinate(38.941, -77.286));
    }

    @Test
    public void testGetDoucmentQuery() {

        Document mongo = new Document();
        CircleTuple obj = new CircleTuple("field", ul, this.distance);
        MongoCircleTuple translator = new MongoCircleTuple(obj);
        translator.getDoucmentQuery(mongo);
    }



}