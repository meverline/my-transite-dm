package me.transit.dao.query.translator.mongo;

import org.bson.Document;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import me.transit.dao.query.tuple.RectangleTuple;

public class MongoRectagleTupleTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);
    private static final GeometryFactory factory_  = new GeometryFactory();
    private Point lr;
    private Point ul;

    @Before
    public void setUp() {
        ul = factory_.createPoint(new Coordinate(38.941, -77.286));
        lr = factory_.createPoint(new Coordinate(38.827, -77.078));
    }

    @Test
    public void testGetDoucmentQuery() {

        Document mongo = new Document();
        RectangleTuple obj = new RectangleTuple("field", ul, lr);
        MongoRectagleTuple translator = new MongoRectagleTuple(obj);
        translator.getDoucmentQuery(mongo);
    }

}