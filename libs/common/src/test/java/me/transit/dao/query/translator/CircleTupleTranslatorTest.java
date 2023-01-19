package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.CircleTuple;
import me.utils.TransiteEnums;
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

import static org.easymock.EasyMock.expect;

public class CircleTupleTranslatorTest extends EasyMockSupport {

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
    public void testGetCriterion() {
        Criteria mongo = this.createNiceMock(Criteria.class);

        expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
        expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
        replayAll();

        CircleTuple obj = new CircleTuple("field", ul, this.distance);
        CircleTupleTranslator translator = new CircleTupleTranslator(obj);
        translator.getCriterion();

        obj = new CircleTuple(String.class, "field", ul, this.distance);
        translator = new CircleTupleTranslator(obj);
        translator.getCriterion();

        obj.getMultipeRestriction(mongo);
        translator.getCriterion();
    }
}