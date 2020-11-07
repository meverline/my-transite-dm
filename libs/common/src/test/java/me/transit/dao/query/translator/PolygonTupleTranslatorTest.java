package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.PolygonBoxTuple;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

public class PolygonTupleTranslatorTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    private List<Point> box = new ArrayList<>();

    private static final GeometryFactory factory_  = new GeometryFactory();

    @Before
    public void setUp() {

        Coordinate[] coords = new Coordinate[5];

        coords[0] = new Coordinate(38.941, 77.078);
        coords[1] = new Coordinate(38.941, -77.286);
        coords[2] = new Coordinate(38.827, -77.286);
        coords[3] = new Coordinate(38.827, -77.078);
        coords[4] = coords[0];
        Polygon poly = factory_.createPolygon(factory_.createLinearRing(coords), null);

        for ( Coordinate cord : poly.getCoordinates()) {
            box.add( factory_.createPoint(cord));
        }

    }

    @Test
    public void testGetCriterion() {

        Criteria mongo = this.createNiceMock(Criteria.class);

        expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
        expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
        replayAll();

        PolygonBoxTuple obj = new PolygonBoxTuple("field", box);
        PolygonTupleTranslator translator = new PolygonTupleTranslator(obj);
        translator.getCriterion();

        obj = new PolygonBoxTuple(String.class, "field", box);
        translator = new PolygonTupleTranslator(obj);
        translator.getCriterion();

        obj.getMultipeRestriction(mongo);
        translator.getCriterion();
    }


}