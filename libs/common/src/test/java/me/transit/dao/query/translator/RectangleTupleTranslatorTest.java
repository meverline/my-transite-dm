package me.transit.dao.query.translator;

import static org.easymock.EasyMock.expect;

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

import me.transit.dao.query.tuple.RectangleTuple;

public class RectangleTupleTranslatorTest extends EasyMockSupport {

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
    public void testGetCriterion() {

        Criteria mongo = this.createNiceMock(Criteria.class);

        expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
        expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
        replayAll();

        RectangleTuple obj = new RectangleTuple("field", ul, lr);
        RectangleTupleTranslator translator = new RectangleTupleTranslator(obj);
        translator.getCriterion();

        obj = new RectangleTuple(String.class, "field", ul, lr);
        translator = new RectangleTupleTranslator(obj);
        translator.getCriterion();

        obj.getMultipeRestriction(mongo);
        translator.getCriterion();
    }

}