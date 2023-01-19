package me.transit.dao.query.translator;

import static org.easymock.EasyMock.expect;

import java.util.Calendar;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.junit.Rule;
import org.junit.Test;

import me.transit.dao.query.tuple.TimeTuple;

public class TimeTupleTranslatorTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

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