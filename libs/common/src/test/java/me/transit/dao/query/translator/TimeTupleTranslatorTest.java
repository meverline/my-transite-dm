package me.transit.dao.query.translator;

import me.transit.dao.query.tuple.TimeTuple;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

public class TimeTupleTranslatorTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    @Test
    public void testConstructor() {
        @SuppressWarnings("unused")
        TimeTuple obj = new TimeTuple("field", Calendar.getInstance(), Calendar.getInstance());
        obj = new TimeTuple(String.class, "field", Calendar.getInstance(), Calendar.getInstance());

    }

    @Test
    public void testGetCriterion() {

        Criteria mongo = this.createNiceMock(Criteria.class);

        expect(mongo.add(EasyMock.anyObject())).andReturn(mongo).anyTimes();
        expect(mongo.createAlias(EasyMock.anyString(), EasyMock.anyString())).andReturn(mongo).anyTimes();
        replayAll();

        TimeTuple obj = new TimeTuple("field", Calendar.getInstance(), Calendar.getInstance());
        TimeTupleTranslator translator = new TimeTupleTranslator(obj);
        translator.getCriterion();

        obj = new TimeTuple(String.class, "field", Calendar.getInstance(), Calendar.getInstance());
        translator = new TimeTupleTranslator(obj);
        translator.getCriterion();
    }


}